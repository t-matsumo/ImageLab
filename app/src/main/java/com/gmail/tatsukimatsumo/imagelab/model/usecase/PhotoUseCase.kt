package com.gmail.tatsukimatsumo.imagelab.model.usecase

import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoDatabaseEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoUseCase(private val repository: PhotoRepository, private val indexRepository: PhotoIndexRepository) {
    enum class SortKey(val repositorySortKey: PhotoIndexRepository.SortKey) {
        SORT_KEY_NONE(PhotoIndexRepository.SortKey.SORT_KEY_NONE),
        SORT_KEY_DATE_ADDED(PhotoIndexRepository.SortKey.SORT_KEY_DATE_ADDED),
        SORT_KEY_DATE_ADDED_DESC(PhotoIndexRepository.SortKey.SORT_KEY_DATE_ADDED_DESC),
    }

    suspend fun getPhotos(sortKey: SortKey) = withContext(Dispatchers.IO) {
        indexRepository.getPhotos(sortKey.repositorySortKey)
    }

    suspend fun refreshImageDatabase() = withContext(Dispatchers.IO) {
        repository
            .getPhotos()
            .map { PhotoDatabaseEntity(it.uri, it.dateAdded) }
            .let { indexRepository.addAll(it) }
    }

    suspend fun deleteImageIndex() = withContext(Dispatchers.IO) {
        indexRepository.deleteAll()
    }
}