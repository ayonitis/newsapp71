package com.creativeinstitute.mynews71.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.creativeinstitute.mynews71.R
import com.creativeinstitute.mynews71.models.Article

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    lateinit var articleImage: ImageView
    lateinit var articleSource: TextView
    lateinit var articleTitle: TextView
    lateinit var articleDescription: TextView
    lateinit var articleDateTime: TextView

    private val differCallBack = object:DiffUtil.ItemCallback<Article>(){
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, differCallBack)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_news, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article) -> Unit)? = null

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article = differ.currentList[position]

        // Find the views
        val articleImage = holder.itemView.findViewById<ImageView>(R.id.articleImage)
        val articleTitle = holder.itemView.findViewById<TextView>(R.id.articleTitle)
        val articleSource = holder.itemView.findViewById<TextView>(R.id.articleSource)
        val articleDescription = holder.itemView.findViewById<TextView>(R.id.articleDescription)
        val articleDateTime = holder.itemView.findViewById<TextView>(R.id.articleDateTime)

        holder.itemView.apply {
            // Handle nullable URL for the image
            val imageUrl = article.urlToImage
            if (!imageUrl.isNullOrEmpty()) {
                Glide.with(this).load(imageUrl).into(articleImage)
            } else {
                // You can load a placeholder or do nothing
                articleImage.setImageResource(R.drawable.baseline_headline_24) // Replace with your placeholder
            }

            // Set the source name, description, title, and published date
            articleSource.text = article.source?.name ?: "Unknown Source"
            articleDescription.text = article.description ?: "No description available"
            articleTitle.text = article.title ?: "No title available"
            articleDateTime.text = article.publishedAt ?: "Unknown Date"

            // Set the click listener
            setOnClickListener {
                onItemClickListener?.let { clickListener ->
                    clickListener(article)
                }
            }
        }

    }

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }

}