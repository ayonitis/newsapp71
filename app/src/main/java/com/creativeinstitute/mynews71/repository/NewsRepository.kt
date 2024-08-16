package com.creativeinstitute.mynews71.repository

import androidx.room.Query
import com.creativeinstitute.mynews71.api.RetrofitInstance
import com.creativeinstitute.mynews71.db.ArticleDatabase
import com.creativeinstitute.mynews71.models.Article
import retrofit2.Retrofit

class NewsRepository(val db: ArticleDatabase) {
    suspend fun getHeadlines(countryCode: String, pageNumber: Int) =
        RetrofitInstance.api.getHeadlines(countryCode,pageNumber)

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        RetrofitInstance.api.searchForNews(searchQuery, pageNumber)

    suspend fun upsert(article: Article) = db.getArticleDao().upsert(article)

    fun getFavoriteNews() = db.getArticleDao().getAllArticles()

    suspend fun deleteArticle(article: Article)= db.getArticleDao().deleteArticle(article)

}