package com.example.githubuser.data.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

	@field:SerializedName("total_count")
	val totalCount: Int,

	@field:SerializedName("items")
	val items: ArrayList<UserItem>
)

data class UserItem(

	@field:SerializedName("login")
	val login: String,

	@field:SerializedName("avatar_url")
	val avatarUrl: String,

	@field:SerializedName("id")
	val id: Int,
)
