package com.android.ntduc.baseproject.ui.component.navigation.fragment.home

import androidx.fragment.app.activityViewModels
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.databinding.FragmentHomeBinding
import com.android.ntduc.baseproject.ui.base.BaseFragment
import com.android.ntduc.baseproject.ui.component.navigation.NavigationViewModel
import com.android.ntduc.baseproject.ui.component.navigation.fragment.home.adapter.HomeAdapter
import com.android.viewpager2transformer.banner.ZoomInTransformer
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val viewModel: NavigationViewModel by activityViewModels()
    private lateinit var homeAdapter: HomeAdapter

    override fun initView() {
        super.initView()

        val listFragment = listOf(ChildHomeFragment(), ChildHomeFragment(), ChildHomeFragment())
        homeAdapter = HomeAdapter(childFragmentManager, lifecycle, listFragment)
        binding.vp.apply {
            adapter = homeAdapter
            setPageTransformer(ZoomInTransformer())
        }
        TabLayoutMediator(binding.tab, binding.vp) { tab, position ->
            tab.text = "Video $position"
        }.attach()
    }
}