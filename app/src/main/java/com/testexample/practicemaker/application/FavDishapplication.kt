package com.testexample.practicemaker.application

import android.app.Application
import com.testexample.practicemaker.model.databse.FavDishRepository
import com.testexample.practicemaker.model.databse.FavDishRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class FavDishapplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())
    private val database by lazy { FavDishRoomDatabase.getDatabase(this,applicationScope) }
    val respository by lazy { FavDishRepository(database.favDishDao()) }
}