package com.example.githubuser.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.data.response.UserItem
import com.example.githubuser.databinding.ActivityMainBinding
import com.example.githubuser.ui.detail.DetailUserActivity
import com.example.githubuser.ui.favorite.FavoriteActivity
import com.example.githubuser.ui.setting.SettingActivity
import com.example.githubuser.ui.setting.SettingPreferences
import com.example.githubuser.ui.setting.SettingViewModel
import com.example.githubuser.ui.setting.ViewModelFactory
import com.example.githubuser.ui.setting.dataStore

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private lateinit var viewModel : UserViewModel
    private lateinit var adapter : UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        adapter.setOnItemClickCallback(object: UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UserItem) {
                Intent(this@MainActivity, DetailUserActivity::class.java).also {
                    it.putExtra(DetailUserActivity.EXTRA_USERNAME, data.login)
                    it.putExtra(DetailUserActivity.EXTRA_ID, data.id)
                    it.putExtra(DetailUserActivity.EXTRA_AVATAR_URL, data.avatarUrl)
                    startActivity(it)
                }
            }
        })

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[UserViewModel::class.java]

        val pref = SettingPreferences.getInstance(application.dataStore)
        val settingViewModel = ViewModelProvider(this, ViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.apply {

            rvUser.layoutManager = LinearLayoutManager(this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            viewModel.setSearchUser("a") // ketika aplikasi pertama kali dijalankan

            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnKeyListener { _, keyCode, event ->
                    if(event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                        searchView.hide()
                        viewModel.setSearchUser(searchView.text.toString())
                        return@setOnKeyListener true
                    }
                    return@setOnKeyListener false
                }
        }

        viewModel.getSearchUser().observe(this) {
            if (it != null) {
                adapter.setList(it)
            }
        }

        viewModel.loading.observe(this) {
            showLoading(it)
        }

        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when(menuItem.itemId) {
                R.id.fav_menu -> {
                    startActivity(Intent(this, FavoriteActivity::class.java))
                    true
                }
                R.id.setting_menu -> {
                    startActivity(Intent(this, SettingActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun showLoading(state : Boolean) {
        when (state) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.GONE
        }
    }
}