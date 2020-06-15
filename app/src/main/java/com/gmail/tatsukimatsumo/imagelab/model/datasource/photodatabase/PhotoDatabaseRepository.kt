package com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoIndexEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey.*
import kotlinx.android.synthetic.main.activity_main.*

class PhotoDatabaseRepository(private val context: Context) : PhotoIndexRepository {
    override val photoList = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .getAllAsync()

//    override suspend fun getPhotosAsync(sortKey: SortKey): LiveData<List<Photo>> {
//        val dao = PhotoDatabase
//            .getDatabase(context)
//            .photoDao()
//
//        var entities = dao.getAll()
//        entities = when (sortKey) {
//            SORT_KEY_NONE -> {
//                entities // do nothing
//            }
//            SORT_KEY_DATE_ADDED,
//            SORT_KEY_DATE_ADDED_DESC -> entities.sortedBy { it.dateAdded }
//            SORT_KEY_NORM -> entities.sortedBy { it.norm }
//        }
//
//        if (sortKey.desc) {
//            entities = entities.reversed()
//        }
//
//        return entities.map { PhotoIndexEntity(it) }
//    }

    override suspend fun addAll(entities: List<PhotoIndexEntity>) = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .addAll(entities.map { Photo(it) })

    override suspend fun insert(entity: PhotoIndexEntity) = PhotoDatabase
    .getDatabase(context)
    .photoDao()
    .insert(Photo(entity))

    override suspend fun deleteAll() = PhotoDatabase
        .getDatabase(context)
        .photoDao()
        .deleteAll()
}