package com.example.githubuser.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.example.githubuser.R
import com.example.githubuser.databinding.ActivityDetailUserBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USERNAME = "extra_username"
        const val EXTRA_ID = "extra_id"
        const val EXTRA_AVATAR_URL = "extra_avatar_url"
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: DetailUserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_USERNAME)
        val id = intent.getIntExtra(EXTRA_ID, 0)
        val avatarUrl = intent.getStringExtra(EXTRA_AVATAR_URL)

        val bundle = Bundle()
        bundle.putString(EXTRA_USERNAME, username)

        viewModel = ViewModelProvider(this)[DetailUserViewModel::class.java]

        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager, bundle)

        if (username != null) {
            viewModel.setUserDetail(username)
        }
        viewModel.getUserDetail().observe(this) {
            if (it != null) {
                binding.apply {
                    showLoading(true)
                    tvDetailName.text = it.name
                    tvDetailUsername.text = it.login
                    tvDetailFollowersCount.text = it.followers.toString()
                    tvDetailFollowingCount.text = it.following.toString()
                    tvDetailFollowing.text = getString(R.string.string_followers)
                    tvDetailFollower.text = getString(R.string.string_following)
                    Glide.with(this@DetailUserActivity)
                        .load(it.avatarUrl)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .centerCrop()
                        .into(ivDetailUserPhotoProfile)
                    viewPager.adapter = sectionPagerAdapter
                    tabs.setupWithViewPager(viewPager)
                    toggleFav.visibility = View.VISIBLE
                    fab.visibility = View.VISIBLE
                    showLoading(false)
                }
            }
        }

        var isChecked = true
        CoroutineScope(Dispatchers.IO).launch {
            val count = viewModel.checkUser(id)
            withContext(Dispatchers.Main){
                if (count != null) {
                    if (count > 0) {
                        binding.toggleFav.isChecked = true
                        isChecked = true
                    } else {
                        binding.toggleFav.isChecked = false
                        isChecked = false
                    }
                }
            }
        }

        binding.toggleFav.setOnClickListener {
            isChecked = !isChecked
            binding.toggleFav.isChecked = isChecked

            if(isChecked) {
                if (username != null && avatarUrl != null) {
                    viewModel.addToFavorite(username, id, avatarUrl)
                    Toast.makeText(this@DetailUserActivity, "Added to Favorite", Toast.LENGTH_SHORT).show()
                }
            } else {
                viewModel.removeFromFavorite(id)
                Toast.makeText(this@DetailUserActivity, "Removed from Favorite", Toast.LENGTH_SHORT).show()
            }
        }

        binding.fab.setOnClickListener {
            val share = Intent(Intent.ACTION_SEND)
            share.type = "text/plain"
            share.putExtra(Intent.EXTRA_TEXT, "Github User Information\n\nUsername: $username\nFollowers: ${binding.tvDetailFollowersCount.text}\nFollowing: ${binding.tvDetailFollowingCount.text}\nLink: github.com/$username\n\nFrom GithubUser Apps\nby Zaidurrohman")
            startActivity(Intent.createChooser(share, "Share User Info"))
        }
    }

    private fun showLoading(state : Boolean) {
        when (state) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.INVISIBLE
        }
    }
}