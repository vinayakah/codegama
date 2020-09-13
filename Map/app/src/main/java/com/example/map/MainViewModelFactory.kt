package com.example.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.map.roodatabase.FeaturedRepository

class MainViewModelFactory(private var repository: FeaturedRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(ActVM::class.java)){
            return ActVM(repository) as T
        }
        throw IllegalArgumentException("Unknown View Model class")

    }
}