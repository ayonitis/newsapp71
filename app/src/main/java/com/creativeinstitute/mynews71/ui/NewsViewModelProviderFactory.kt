package com.creativeinstitute.mynews71.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.creativeinstitute.mynews71.repository.NewsRepository

class NewsViewModelProviderFactory(val app: Application, val newsRepository: NewsRepository): ViewModelProvider.Factory {

    override fun <T: ViewModel> create(modelClass:Class<T>): T{
        return NewsViewModel(app, newsRepository) as T
    }
}