package com.android.ntduc.baseproject.ui.component.navigation

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.ntduc.baseproject.constant.FileType
import com.android.ntduc.baseproject.data.DataRepositorySource
import com.android.ntduc.baseproject.data.Resource
import com.android.ntduc.baseproject.data.dto.file.BaseFile
import com.android.ntduc.baseproject.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NavigationViewModel @Inject constructor(
    private val repository: DataRepositorySource
) : BaseViewModel() {
    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    val filesLiveDataPrivate = MutableLiveData<Resource<List<BaseFile>>>()
    val filesLiveData: LiveData<Resource<List<BaseFile>>> get() = filesLiveDataPrivate

    fun requestAllFiles() {
        viewModelScope.launch {
            filesLiveDataPrivate.value = Resource.Loading()
            repository.requestAllFiles(listOf(*FileType.ALL)).collect {
                filesLiveDataPrivate.value = it
            }
        }
    }
}