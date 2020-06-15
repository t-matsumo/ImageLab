package com.gmail.tatsukimatsumo.imagelab.model.usecase

import androidx.lifecycle.LiveData
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
    val progress: LiveData<Int> = _progress

    val photoList = indexRepository.photoList

//    suspend fun getPhotos(sortKey: SortKey) = withContext(Dispatchers.IO) {
//        indexRepository.getPhotosAsync(sortKey)
//    }

    suspend fun refreshImageDatabase() = withContext(Dispatchers.IO) {
        val photos = repository.getPhotos()
        val count = photos.size
        withContext(Dispatchers.Main) {
            _progress.value = 0
        }

        for ((i, p) in photos.withIndex()) {
            val entity = createPhotoDatabaseEntity(p)
            indexRepository.insert(entity)

            withContext(Dispatchers.Main) {
                val progressValue = 100 * (i + 1) / count
                if (progressValue != _progress.value) {
                    _progress.value = progressValue
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