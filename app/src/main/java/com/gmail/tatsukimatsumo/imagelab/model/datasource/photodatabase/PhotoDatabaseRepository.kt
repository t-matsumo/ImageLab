package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import androidx.core.net.toUri
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository

class PhotoDatabaseRepository(private val context: Context) : PhotoIndexRepository {
    override suspend fun getPhotos() = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .getAll()
        .map { PhotoIndexRepository.PhotoDatabaseEntity(it.uriString.toUri()) }

    override suspend fun addAll(entities: List<PhotoIndexRepository.PhotoDatabaseEntity>) = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .addAll(entities.mapIndexed { index, v ->  Photo(index, v.uri.toString()) })

    override suspend fun deleteAll() = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .deleteAll()
}