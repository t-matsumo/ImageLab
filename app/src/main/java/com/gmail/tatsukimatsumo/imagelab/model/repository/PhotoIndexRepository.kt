package com.gmail.tatsukimatsumo.imagelab.model.repository

import android.net.Uri

interface PhotoIndexRepository {
    data class PhotoDatabaseEntity(val uri: Uri)

    suspend fun getPhotos(): List<PhotoDatabaseEntity>
    suspend fun addAll(entities: List<PhotoDatabaseEntity>)
    suspend fun deleteAll()
}