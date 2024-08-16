package com.creativeinstitute.mynews71.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.webkit.WebViewClient
import androidx.navigation.fragment.navArgs
import com.creativeinstitute.mynews71.R
import com.creativeinstitute.mynews71.databinding.FragmentArticleBinding
import com.creativeinstitute.mynews71.models.Article
import com.creativeinstitute.mynews71.ui.NewsActivity
import com.creativeinstitute.mynews71.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private lateinit var newsViewModel: NewsViewModel

    // Access the arguments
    private val args: ArticleFragmentArgs by navArgs()
    lateinit var binding: FragmentArticleBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentArticleBinding.bind(view)

        // Ensure args.article is correctly treated as an Article object
        val article = args.article as Article

        newsViewModel = (activity as NewsActivity).newsViewModel

        // Load the article's URL in the WebView
        binding.webView.apply {
            webViewClient = WebViewClient()
            article.url?.toHttpUrlOrNull()?.let {
                loadUrl(it.toString())
            }
        }

        // Handle the floating action button click
        binding.fab.setOnClickListener {
            newsViewModel.addToFavorite(article)  // Ensure the article is passed correctly
            Snackbar.make(view, "Added to Favorites", Snackbar.LENGTH_SHORT).show()
        }
    }
}
