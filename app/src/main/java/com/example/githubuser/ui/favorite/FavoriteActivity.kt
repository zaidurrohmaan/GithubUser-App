package com.example.githubuser.ui.favorite

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.data.FavoriteUser
import com.example.githubuser.data.response.UserItem
import com.example.githubuser.databinding.ActivityFavoriteBinding
import com.example.githubuser.ui.detail.DetailUserActivity
import com.example.githubuser.ui.main.UserAdapter

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter : UserAdapter
    private lateinit var viewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        viewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback{
            override fun onItemClicked(data: UserItem) {
                Intent(this@FavoriteActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        binding.apply {
            rvFavoriteUser.setHasFixedSize(true)
            rvFavoriteUser.layoutManager = LinearLayoutManager(this@FavoriteActivity)
            rvFavoriteUser.adapter = adapter
        }

        viewModel.getAllFavoriteUser()?.observe(this) {
            if (it != null) {
                adapter.setList(toArrayList(it))
            }
        }
    }

    private fun toArrayList(it: List<FavoriteUser>): ArrayList<UserItem> {
        val listUser = ArrayList<UserItem>()
        for(user in it) {
            val users = UserItem(
                user.login,
                user.avatar_url,
                user.id
            )
            listUser.add(users)
        }
        return listUser
    }
}