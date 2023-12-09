package com.example.githubuser.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.UserItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersViewModel: ViewModel() {
    val listFollowers = MutableLiveData<ArrayList<UserItem>>()
    val listFollowing = MutableLiveData<ArrayList<UserItem>>()

    fun setListFollowers(username: String) {
        ApiConfig.apiInstance
            .getFollowers(username)
            .enqueue(object: Callback<ArrayList<UserItem>>{
                override fun onResponse(
                    call: Call<ArrayList<UserItem>>,
                    response: Response<ArrayList<UserItem>>
                ) {
                    if(response.isSuccessful) {
                        listFollowers.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserItem>>, t: Throwable) {
                    Log.d("onFollowersViewModel", "onFailure: ${t.message}")
                }
            })
    }

    fun setListFollowing(username: String) {
        ApiConfig.apiInstance
            .getFollowing(username)
            .enqueue(object: Callback<ArrayList<UserItem>>{
                override fun onResponse(
                    call: Call<ArrayList<UserItem>>,
                    response: Response<ArrayList<UserItem>>
                ) {
                    if(response.isSuccessful) {
                        listFollowing.postValue(response.body())
                    }
                }

                override fun onFailure(call: Call<ArrayList<UserItem>>, t: Throwable) {
                    Log.d("onFollowersViewModel", "onFailure: ${t.message}")
                }
            })
    }

    fun getListFollowers(): LiveData<ArrayList<UserItem>>{
        return listFollowers
    }

    fun getListFollowing(): LiveData<ArrayList<UserItem>>{
        return listFollowing
    }
}