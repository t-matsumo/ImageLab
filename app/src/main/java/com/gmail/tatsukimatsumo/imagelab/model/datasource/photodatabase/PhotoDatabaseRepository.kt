package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import androidx.core.net.toUri
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoDatabaseEntity

class PhotoDatabaseRepository(private val context: Context) : PhotoIndexRepository {
    override suspend fun getPhotos(sortKey: PhotoIndexRepository.SortKey): List<PhotoDatabaseEntity> {
        val dao = PhotoDatabase
            .getDatabase(context)
            .photoDao()

        var entities = dao.getAll()
        entities = if (sortKey.desc) {
            entities.sortedByDescending { it.date_added }
        } else {
            entities.sortedBy { it.date_added }
        }

        return entities.map { PhotoDatabaseEntity(it.uriString.toUri(), it.date_added) }
    }

    override suspend fun addAll(entities: List<PhotoDatabaseEntity>) = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .addAll(entities.mapIndexed { index, v ->  Photo(index, v.uri.toString(), v.dateAdded) })

    override suspend fun deleteAll() = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .deleteAll()
}