package com.gmail.tatsukimatsumo.imagelab.model.usecase

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.gmail.tatsukimatsumo.imagelab.model.imageloder.ImageLoader
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoDatabaseEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoRepository
import com.gmail.tatsukimatsumo.imagelab.viewmodel.PhotoListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.opencv.android.OpenCVLoader
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat

class PhotoUseCase(
    private val repository: PhotoRepository,
    private val indexRepository: PhotoIndexRepository,
    private val imageLoader: ImageLoader
) {
    enum class SortKey(val repositorySortKey: PhotoIndexRepository.SortKey) {
        SORT_KEY_NONE(PhotoIndexRepository.SortKey.SORT_KEY_NONE),
        SORT_KEY_DATE_ADDED(PhotoIndexRepository.SortKey.SORT_KEY_DATE_ADDED),
        SORT_KEY_DATE_ADDED_DESC(PhotoIndexRepository.SortKey.SORT_KEY_DATE_ADDED_DESC),
        SORT_KEY_NORM(PhotoIndexRepository.SortKey.SORT_KEY_NORM)
    }

    val progress: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>().also { it.value = 100 }
    }

    suspend fun getPhotos(sortKey: SortKey) = withContext(Dispatchers.IO) {
        indexRepository.getPhotos(sortKey.repositorySortKey)
    }

    suspend fun refreshImageDatabase() = withContext(Dispatchers.IO) {
            val photos = repository.getPhotos()
            val count = photos.size
            var current = 0
            withContext(Dispatchers.Main) {
                progress.value = 0
            }
            photos
                .map {
                    val entity = createPhotoDatabaseEntity(it)

                    current++
                    withContext(Dispatchers.Main) {
                        progress.value = 100 * current / count
                    }

                    entity
                }
                .let { indexRepository.addAll(it) }
        }

    suspend fun deleteImageIndex() = withContext(Dispatchers.IO) {
        indexRepository.deleteAll()
    }

    private suspend fun createPhotoDatabaseEntity(photo: PhotoRepository.PhotoEntity): PhotoDatabaseEntity {
        val bitmap = imageLoader.loadBitmap(photo.uri)
        val mat = Mat()
        Utils.bitmapToMat(bitmap, mat)
        val norm = Core.norm(mat).toInt()

        return PhotoDatabaseEntity(photo.uri, photo.dateAdded, norm)
    }
}