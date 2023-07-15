package com.android.ntduc.baseproject.data.remote

import com.android.ntduc.baseproject.data.Resource
import com.android.ntduc.baseproject.data.dto.frames.DataFrames

internal interface RemoteDataSource {
    suspend fun requestFrames(): Resource<DataFrames>
}