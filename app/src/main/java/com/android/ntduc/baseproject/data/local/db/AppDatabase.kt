package com.android.ntduc.baseproject.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.ntduc.baseproject.data.dto.app.BaseApp
import com.android.ntduc.baseproject.data.dto.file.BaseFile
import com.android.ntduc.baseproject.data.local.db.dao.BaseAppDao
import com.android.ntduc.baseproject.data.local.db.dao.BaseFileDao

@Database(entities = [BaseFile::class, BaseApp::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val baseFileDao: BaseFileDao
    abstract val baseAppDao: BaseAppDao

    companion object {
        const val DB_NAME = "BaseFileDatabase.db"
    }
}