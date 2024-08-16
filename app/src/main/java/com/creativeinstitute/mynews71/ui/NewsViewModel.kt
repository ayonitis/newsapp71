package com.creativeinstitute.mynews71.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.creativeinstitute.mynews71.models.Article
import com.creativeinstitute.mynews71.models.NewsResponse
import com.creativeinstitute.mynews71.repository.NewsRepository
import com.creativeinstitute.mynews71.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class NewsViewModel(app: Application, val newsRepository: NewsRepository) : AndroidViewModel(app) {

    val headline: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var headlinesPage = 1
    var headlinesResponse: NewsResponse? = null

    val searchNews: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var newSearchQuery: String? = null
    var oldSearchQuery: String? = null

    fun getHeadlines(countryCode: String) = viewModelScope.launch {
        headlinesInternet(countryCode)
    }

    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNewsInternet(searchQuery)
    }

    init{
        getHeadlines("us")
    }

    private fun handleHeadlinesResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {
                resultResponse ->
                headlinesPage++
                if(headlinesResponse == null){
                    headlinesResponse = resultResponse
                } else{
                    val oldArticles = headlinesResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                return Resource.Success(headlinesResponse?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun addToFavorite(article: Article) = viewModelScope.launch{
        newsRepository.upsert(article)
    }

    fun getFavoriteNews() = newsRepository.getFavoriteNews()

    fun deleteArticle(article:Article) = viewModelScope.launch{
        newsRepository.deleteArticle(article)
    }

    fun internetConnection(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager
        return connectivityManager?.getNetworkCapabilities(connectivityManager.activeNetwork)?.run {
            when {
                hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } ?: false
    }

    private suspend fun headlinesInternet(countryCode: String) {
        headline.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRepository.getHeadlines(countryCode, headlinesPage)
                headline.postValue(handleHeadlinesResponse(response))
            } else {
                headline.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> headline.postValue(Resource.Error("Unable to connect"))
                else -> headline.postValue(Resource.Error("No signal"))
            }
        }
    }

    private suspend fun searchNewsInternet(searchQuery: String){
        newSearchQuery = searchQuery
        searchNews.postValue(Resource.Loading())
        try {
            if (internetConnection(this.getApplication())) {
                val response = newsRepository.getHeadlines(searchQuery, searchNewsPage)
                searchNews.postValue(handleHeadlinesResponse(response))
            } else {
                searchNews.postValue(Resource.Error("No internet connection"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> searchNews.postValue(Resource.Error("Unable to connect"))
                else -> searchNews.postValue(Resource.Error("No signal"))
            }
        }
    }
}