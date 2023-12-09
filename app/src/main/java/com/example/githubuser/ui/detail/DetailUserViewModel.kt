package com.example.githubuser.ui.detail

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubuser.data.FavoriteUser
import com.example.githubuser.data.FavoriteUserDao
import com.example.githubuser.data.UserDatabase
import com.example.githubuser.data.response.DetailResponse
import com.example.githubuser.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailUserViewModel(application: Application): AndroidViewModel(application) {
    val user = MutableLiveData<DetailResponse>()

    private var userDao: FavoriteUserDao?
    private var userDb: UserDatabase?

    init {
        userDb = UserDatabase.getDatabase(application)
        userDao = userDb?.favoriteUserDao()
    }

    fun setUserDetail(username: String) {
        ApiConfig.apiInstance
            .getDetailUser(username)
            .enqueue(object: Callback<DetailResponse>{
                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if(response.isSuccessful) {
                        user.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    Log.d("DetailUserViewModel", "onFailure: ${t.message}")
                }
            })
    }

    fun getUserDetail(): LiveData<DetailResponse> {
        return user
    }

    fun addToFavorite(username: String, id: Int, avatarUrl: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = FavoriteUser(
                id,
                username,
                avatarUrl
            )
            userDao?.addToFavorite(user)
        }
    }

    fun checkUser(id: Int) = userDao?.checkUser(id)

    fun removeFromFavorite(id: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            userDao?.removeFromFavorite(id)
        }
    }
}