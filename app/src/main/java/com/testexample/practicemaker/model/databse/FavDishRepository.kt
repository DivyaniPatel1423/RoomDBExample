package com.testexample.practicemaker.model.databse

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.testexample.practicemaker.model.entities.FavDish
import kotlinx.coroutines.flow.Flow

class FavDishRepository(val favDishDao: FavDishDao) {

    val allDishesList : LiveData<List<FavDish>> = favDishDao.getAllFavDishesList().asLiveData()


    @WorkerThread
    suspend fun insertFavDishData(favDish : FavDish){
        favDishDao.insertFavDishDetails(favDish)
    }

    @WorkerThread
    suspend fun updateFavDishData(favDish : FavDish){
        favDishDao.update(favDish)
    }
    @WorkerThread
    suspend fun deleteFavDishData(favDish : FavDish){
        favDishDao.delete(favDish)
    }

}