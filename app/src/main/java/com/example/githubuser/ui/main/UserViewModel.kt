package com.example.githubuser.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.UserItem
import com.example.githubuser.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<UserItem>>()
    val loading = MutableLiveData<Boolean>()

    fun setSearchUser(query: String) {

        loading.value = true

        ApiConfig.apiInstance
            .getUsers(query)
            .enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if(response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                    }

                    loading.postValue(false)
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d("onUserViewModel", "onFailure: ${t.message}")
                }
            })
    }

    fun getSearchUser(): LiveData<ArrayList<UserItem>> {
        return listUsers
    }
}