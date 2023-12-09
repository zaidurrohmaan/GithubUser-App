package com.example.githubuser.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubuser.data.FavoriteUser
import com.example.githubuser.data.FavoriteUserDao
import com.example.githubuser.data.UserDatabase

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun getAllFavoriteUser(): LiveData<List<FavoriteUser>>? {
        return userDao?.getAllFavoriteUser()
    }
}