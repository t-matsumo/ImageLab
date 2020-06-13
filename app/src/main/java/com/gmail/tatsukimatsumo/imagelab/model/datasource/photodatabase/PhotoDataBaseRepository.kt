package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository

class PhotoDataBaseRepository(private val context: Context) : PhotoRepository {
    override suspend fun getPhotos() = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .getAll()
        .map { PhotoRepository.PhotoEntity(it.uriString.toUri()) }
}