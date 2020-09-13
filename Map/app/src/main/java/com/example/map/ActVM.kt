package com.example.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.map.roodatabase.FeaturedEntity
import com.example.map.roodatabase.FeaturedRepository
import kotlinx.coroutines.launch

class ActVM(private val repository: FeaturedRepository) :ViewModel() {


    val features =  repository.features


    fun storeAddresses(location: String) {
            insertData(FeaturedEntity(0, location))
        
    }

    private fun insertData(featuredEntity: FeaturedEntity) {
        viewModelScope.launch {
            repository.insert(featuredEntity)
        }
    }
}