package com.gmail.tatsukimatsumo.imagelab.model.datasource

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository

class PhotoContentsProviderClientRepository(private val context: Context) : PhotoRepository {
    override suspend fun getPhotos(): List<PhotoRepository.PhotoEntity> {
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            null,
            null,
            null,
            null)

        if (cursor == null || cursor.moveToFirst() == false) {
            return emptyList()
        }

        val photos = mutableListOf<PhotoRepository.PhotoEntity>()
        do {
            val idIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(idIndex)
            val uri: Uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            photos.add(PhotoRepository.PhotoEntity(uri))
        } while (cursor.moveToNext())
        cursor.close()

        return photos
    }
}