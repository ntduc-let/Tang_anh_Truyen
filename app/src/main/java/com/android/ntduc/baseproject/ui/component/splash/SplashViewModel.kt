package com.android.ntduc.baseproject.ui.component.splash

import com.android.ntduc.baseproject.data.DataRepositorySource
import com.android.ntduc.baseproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: DataRepositorySource
) : BaseViewModel()