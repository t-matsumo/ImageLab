package com.gmail.tatsukimatsumo.imagelab.model.usecase

import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoUseCase(private val repository: PhotoRepository, private val indexRepository: PhotoIndexRepository) {
    suspend fun getPhotos() = withContext(Dispatchers.IO) {
        indexRepository.getPhotos()
    }

    suspend fun refreshImageDatabase() = withContext(Dispatchers.IO) {
        repository
            .getPhotos()
            .map { PhotoIndexRepository.PhotoDatabaseEntity(it.uri) }
            .let { indexRepository.addAll(it) }
    }

    suspend fun deleteImageIndex() = withContext(Dispatchers.IO) {
        indexRepository.deleteAll()
    }
}