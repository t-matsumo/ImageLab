package com.gmail.tatsukimatsumo.imagelab.model.repository

import android.net.Uri
import androidx.lifecycle.LiveData

interface PhotoRepository {
    data class PhotoEntity(val uri: Uri)

    suspend fun getPhotos(): List<PhotoEntity>
}