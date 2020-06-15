package com.gmail.tatsukimatsumo.imagelab.model.usecase

import androidx.lifecycle.MutableLiveData
import com.gmail.tatsukimatsumo.imagelab.model.imageloder.ImageLoader
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoIndexEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat

class PhotoUseCase(
    private val repository: PhotoRepository,
    private val indexRepository: PhotoIndexRepository,
    private val imageLoader: ImageLoader
) {
    private val _progress = MutableLiveData<Int>().also { it.value = 100 }
    val progress = _progress

    fun getPhotosLiveDataSortedBy(sortKey: SortKey) = indexRepository.getPhotosLiveDataSortedBy(sortKey)

    suspend fun refreshImageDatabase() = withContext(Dispatchers.IO) {
        val photos = repository.getPhotos()

        // まだ存在していないデータのみ挿入
        val photoUriSet = photos.map { it.uri }.toSet()
        val photoIndexUriSet = indexRepository.getAllPhotos().map { it.uri }.toSet()
        val tmpSet = photoUriSet - photoIndexUriSet

        val insertingPhoto = photos.filter { tmpSet.contains(it.uri) }
        val count = insertingPhoto.size
        if (count != 0) {
            _progress.postValue(0)
            for ((i, p) in insertingPhoto.withIndex()) {
                val entity = createPhotoDatabaseEntity(p)
                indexRepository.insert(entity)

                val progressValue = 100 * (i + 1) / count
                if (progressValue != _progress.value) {
                    _progress.postValue(progressValue)
                }
            }
        }
    }

    suspend fun deleteImageIndex() = withContext(Dispatchers.IO) {
        indexRepository.deleteAll()
    }

    private suspend fun createPhotoDatabaseEntity(photo: PhotoRepository.PhotoEntity): PhotoIndexEntity {
        val bitmap = imageLoader.loadBitmap(photo.uri)
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        val norm = Core.norm(mat).toInt()

        return PhotoIndexEntity(photo.uri, photo.dateAdded, norm)
    }
}