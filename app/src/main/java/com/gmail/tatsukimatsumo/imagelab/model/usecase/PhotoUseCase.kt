package com.gmail.tatsukimatsumo.imagelab.model.usecase

import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PhotoUseCase(private val repository: PhotoRepository) {
    suspend fun getPhotos() = withContext(Dispatchers.IO) {
        repository.getPhotos()
    }
}