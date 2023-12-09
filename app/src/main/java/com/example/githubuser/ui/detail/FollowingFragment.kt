package com.example.githubuser.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuser.R
import com.example.githubuser.databinding.FragmentFollowBinding
import com.example.githubuser.ui.main.UserAdapter

class FollowingFragment : Fragment(R.layout.fragment_follow) {

    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowersViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val arg = arguments
        username = arg?.getString(DetailUserActivity.EXTRA_USERNAME).toString()

        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentFollowBinding.bind(view)

        adapter = UserAdapter()
        adapter.notifyDataSetChanged()

        binding.apply {
            rvListFollow.setHasFixedSize(true)
            rvListFollow.layoutManager = LinearLayoutManager(activity)
            rvListFollow.adapter = adapter
        }

        showLoading(true)
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[FollowersViewModel::class.java]
        viewModel.setListFollowing(username)
        viewModel.getListFollowing().observe(viewLifecycleOwner) {
            if (it != null) {
                adapter.setList(it)
                showLoading(false)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showLoading(state : Boolean) {
        when (state) {
            true -> binding.progressBar.visibility = View.VISIBLE
            false -> binding.progressBar.visibility = View.INVISIBLE
        }
    }
}