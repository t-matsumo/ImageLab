package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoIndexEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey.*

class PhotoDatabaseRepository(private val context: Context) : PhotoIndexRepository {
    private val dao = PhotoDatabase
        .getDatabase(context)
        .photoDao()

    override fun getPhotosLiveDataSortedBy(sortKey: SortKey) = when (sortKey) {
        SORT_KEY_NONE -> dao.getAllAsync()
        SORT_KEY_DATE_ADDED -> dao.getAllAsyncSortedByDateAdded()
        SORT_KEY_DATE_ADDED_DESC -> dao.getAllAsyncSortedByDateAddedDesc()
        SORT_KEY_NORM -> dao.getAllAsyncSortedByNorm()
    }

    override suspend fun getAllPhotos() = dao.getAllPhotos()
    override suspend fun insert(entity: PhotoIndexEntity) = dao.insert(Photo(entity))
    override suspend fun deleteAll() = dao.deleteAll()
}