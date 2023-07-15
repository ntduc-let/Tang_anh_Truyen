package com.android.ntduc.baseproject.ui.component.navigation.fragment

import androidx.fragment.app.activityViewModels
import com.android.ntduc.baseproject.R
import com.android.ntduc.baseproject.databinding.FragmentDetailBinding
import com.android.ntduc.baseproject.ui.base.BaseFragment
import com.android.ntduc.baseproject.ui.component.navigation.NavigationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : BaseFragment<FragmentDetailBinding>(R.layout.fragment_detail) {
    private val viewModel: NavigationViewModel by activityViewModels()
}