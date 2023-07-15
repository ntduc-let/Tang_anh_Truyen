package com.android.ntduc.baseproject.data.local

import android.content.Context
import com.android.ntduc.baseproject.data.Resource
import com.android.ntduc.baseproject.data.dto.file.BaseFile
import com.android.ntduc.baseproject.data.local.db.dao.BaseAppDao
import com.android.ntduc.baseproject.data.local.db.dao.BaseFileDao
import com.android.ntduc.baseproject.utils.file.getFiles
import javax.inject.Inject

class LocalData @Inject constructor(private val context: Context, private val baseFileDao: BaseFileDao, private val baseAppDao: BaseAppDao) {
    fun requestAllFiles(types: List<String>): Resource<List<BaseFile>> {
        baseFileDao.deleteAll()
        val baseFileList = context.getFiles(types = types)
        baseFileList.forEach {
            baseFileDao.insert(it)
        }
        return Resource.Success(baseFileList)
    }
}

