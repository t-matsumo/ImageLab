package com.gmail.tatsukimatsumo.imagelab.model.repository

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase.Photo

interface PhotoIndexRepository {
    enum class SortKey(val desc: Boolean = false) {
        SORT_KEY_NONE,
        SORT_KEY_DATE_ADDED,
        SORT_KEY_DATE_ADDED_DESC(true),
        SORT_KEY_NORM,
    }

    data class PhotoIndexEntity(
        val uri: Uri,
        val dateAdded: Int,
        val norm: Int
    ) {
        constructor(photo: Photo) : this(photo.uriString.toUri(), photo.dateAdded, photo.norm)
    }

    val photoList: LiveData<List<Photo>>
//    suspend fun getPhotosAsync(sortKey: SortKey): LiveData<List<Photo>>
    suspend fun addAll(entities: List<PhotoIndexEntity>)
    suspend fun insert(entity: PhotoIndexEntity)
    suspend fun deleteAll()
}