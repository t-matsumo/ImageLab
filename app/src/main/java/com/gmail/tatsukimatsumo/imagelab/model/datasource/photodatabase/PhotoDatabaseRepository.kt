package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import androidx.core.net.toUri
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoDatabaseEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey.*

class PhotoDatabaseRepository(private val context: Context) : PhotoIndexRepository {
    override suspend fun getPhotos(sortKey: SortKey): List<PhotoDatabaseEntity> {
        val dao = PhotoDatabase
            .getDatabase(context)
            .photoDao()

        var entities = dao.getAll()
        entities = when (sortKey) {
            SORT_KEY_NONE -> {
                entities // do nothing
            }
            SORT_KEY_DATE_ADDED,
            SORT_KEY_DATE_ADDED_DESC -> entities.sortedBy { it.date_added }
            SORT_KEY_NORM -> entities.sortedBy { it.norm }
        }

        if (sortKey.desc) {
            entities = entities.reversed()
        }

        return entities.map { PhotoDatabaseEntity(it.uriString.toUri(), it.date_added, it.norm) }
    }

    override suspend fun addAll(entities: List<PhotoDatabaseEntity>) = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .addAll(entities.mapIndexed { index, v ->  Photo(index, v.uri.toString(), v.dateAdded, v.norm) })

    override suspend fun deleteAll() = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .deleteAll()
}