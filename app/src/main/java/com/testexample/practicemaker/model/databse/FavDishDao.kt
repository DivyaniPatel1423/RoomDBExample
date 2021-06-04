package com.testexample.practicemaker.model.databse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.testexample.practicemaker.model.entities.FavDish
import kotlinx.coroutines.flow.Flow


@Dao
interface FavDishDao {
     @Insert(onConflict = OnConflictStrategy.REPLACE)
     suspend fun insertFavDishDetails(favDish : FavDish)

     @Query("SELECT * FROM fav_dishes_table ORDER BY ID")
     fun getAllFavDishesList(): Flow<List<FavDish>>


     @Delete
     suspend fun delete(vararg favDish: FavDish)

     @Update
     suspend fun update(vararg favDish: FavDish)

     @Query("DELETE FROM fav_dishes_table")
     suspend fun deleteAll()
}