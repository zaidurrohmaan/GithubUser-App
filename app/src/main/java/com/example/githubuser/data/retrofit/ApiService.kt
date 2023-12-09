package com.example.githubuser.data.retrofit

import com.example.githubuser.BuildConfig
import com.example.githubuser.data.response.DetailResponse
import com.example.githubuser.data.response.GithubResponse
import com.example.githubuser.data.response.UserItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

const val token = BuildConfig.KEY

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token $token")
    fun getUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    @GET("users/{username}")
    @Headers("Authorization: token $token")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailResponse>

    @GET("users/{username}/followers")
    @Headers("Authorization: token $token")
    fun getFollowers(
        @Path("username") username: String
    ): Call<ArrayList<UserItem>>

    @GET("users/{username}/following")
    @Headers("Authorization: token $token")
    fun getFollowing(
        @Path("username") username: String
    ): Call<ArrayList<UserItem>>
}