package com.testexample.practicemaker.viewmodel

import androidx.lifecycle.*
import com.testexample.practicemaker.model.databse.FavDishRepository
import com.testexample.practicemaker.model.entities.FavDish
import kotlinx.coroutines.launch

class FavDishViewModel(private val repository : FavDishRepository) : ViewModel(){

    val allFavDishes: LiveData<List<FavDish>> = repository.allDishesList

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(favDish: FavDish) = viewModelScope.launch {
        repository.insertFavDishData(favDish)
    }
    fun update(favDish: FavDish) = viewModelScope.launch {
        repository.updateFavDishData(favDish)
    }
    fun delete(favDish: FavDish) = viewModelScope.launch {
        repository.deleteFavDishData(favDish)
    }

    class FavDishViewModelFactory(private val repository: FavDishRepository) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(FavDishViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return FavDishViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}