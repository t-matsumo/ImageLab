package com.gmail.tatsukimatsumo.imagelab.model.repository

import android.net.Uri

interface PhotoRepository {
    data class PhotoEntity(val uri: Uri)

    suspend fun getPhotos(): List<PhotoEntity>
}