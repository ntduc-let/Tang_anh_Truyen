package com.ntduc.baseproject.utils

class RemoteConfigManager {
//
//    private val remoteConfig = FirebaseRemoteConfig.getInstance()
//
//    fun fetchRemoteConfig(onComplete: () -> Unit) {
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = if (BuildConfig.DEBUG) 0 else 0
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.fetchAndActivate().addOnCompleteListener {
//            onComplete()
//        }
//    }
//
//    fun getKey(): List<String> {
//        val json = remoteConfig.getString("config_api_key")
//        val keys = Gson().fromJson(json, Array<String>::class.java) ?: return emptyList()
//        return keys.toList()
//    }
//
//    fun getSurpriseMeImage(): List<String> {
//        val json = remoteConfig.getString("surprise_me_image")
//        val data = Gson().fromJson(json, Array<String>::class.java) ?: return emptyList()
//        return data.toList()
//    }
//
//    fun getListData(context: Context): Array<DataHomeRemote> {
//        val jsonString = remoteConfig.getString("config_home_from_ver111")
//        val result = Gson().fromJson(jsonString, Array<DataHomeRemote>::class.java)
//        return result ?: setDefaultHomeValue(context)
//    }
//
//    fun getCredit(): Credit {
//        val json = remoteConfig.getString("config_credit")
//        return Gson().fromJson(json, Credit::class.java) ?: return Credit()
//    }
//
//    fun getDataSuggest(context: Context, key: String): Array<SuggestRemote> {
//        val jsonString = remoteConfig.getString(key)
//        val result = Gson().fromJson(jsonString, Array<SuggestRemote>::class.java)
//        return result ?: setDefaultSuggestValue(context, key)
//    }
//
//    private fun setDefaultHomeValue(context: Context): Array<DataHomeRemote> {
//        val localResult = readFileRemoteConfigDefaultLocal(context, "config_data_home_default")
//        return if (localResult.isNotEmpty()) {
//            val result = Gson().fromJson(localResult, Array<DataHomeRemote>::class.java)
//            result
//        } else {
//            arrayOf()
//        }
//    }
//
//    private fun setDefaultSuggestValue(activity: Context, fileName: String): Array<SuggestRemote> {
//        val localResult = readFileRemoteConfigDefaultLocal(activity, fileName)
//        return if (localResult.isNotEmpty()) {
//            val result = Gson().fromJson(localResult, Array<SuggestRemote>::class.java)
//            result
//        } else {
//            arrayOf()
//        }
//    }
//
//    private fun readFileRemoteConfigDefaultLocal(context: Context, fileName: String): String {
//        try {
//            val ins = context.resources.openRawResource(
//                context.resources.getIdentifier(
//                    fileName,
//                    "raw", context.packageName
//                )
//            )
//            var text: String
//            ins.bufferedReader().use {
//                text = it.readText()
//                it.close()
//            }
//            ins.close()
//            return text
//        } catch (e: Exception) {
//            e.printStackTrace()
//            return ""
//        }
//    }

}