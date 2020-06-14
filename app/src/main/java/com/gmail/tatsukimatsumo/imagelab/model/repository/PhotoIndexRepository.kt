package com.gmail.tatsukimatsumo.imagelab.model.repository

import android.net.Uri

interface PhotoIndexRepository {
    enum class SortKey(val desc: Boolean = false) {
        SORT_KEY_NONE,
        SORT_KEY_DATE_ADDED,
        SORT_KEY_DATE_ADDED_DESC(true),
        SORT_KEY_NORM,
    }

    data class PhotoDatabaseEntity(val uri: Uri, val dateAdded: Int, val norm: Int)

    suspend fun getPhotos(sortKey: SortKey): List<PhotoDatabaseEntity>
    suspend fun addAll(entities: List<PhotoDatabaseEntity>)
    suspend fun deleteAll()
}