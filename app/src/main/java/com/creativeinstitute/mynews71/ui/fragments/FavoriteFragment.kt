package com.creativeinstitute.mynews71.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.creativeinstitute.mynews71.R
import com.creativeinstitute.mynews71.adapters.NewsAdapter
import com.creativeinstitute.mynews71.databinding.FragmentFavoriteBinding
import com.creativeinstitute.mynews71.ui.NewsActivity
import com.creativeinstitute.mynews71.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment(R.layout.fragment_favorite) {
    lateinit var newsViewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentFavoriteBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoriteBinding.bind(view)

        newsViewModel = (activity as NewsActivity).newsViewModel
        setupFavoriteRecycler()

        newsAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("article", it)
            }
            findNavController().navigate(R.id.action_favoriteFragment_to_articleFragment, bundle)
        }

        val itemTouchHelperCallBack = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                newsViewModel.deleteArticle(article)
                Snackbar.make(view, "Removed from Favorites", Snackbar.LENGTH_LONG).apply {

                    setAction("Undo"){
                        newsViewModel.addToFavorite(article)
                    }
                    show()
                }

            }
        }
        ItemTouchHelper(itemTouchHelperCallBack).apply {
            attachToRecyclerView(binding.recyclerFavourites)
        }
        newsViewModel.getFavoriteNews().observe(viewLifecycleOwner, Observer {
            articles -> newsAdapter.differ.submitList(articles)
        })
}


    private fun setupFavoriteRecycler(){
        newsAdapter = NewsAdapter()
        binding.recyclerFavourites.apply{
            adapter = newsAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}