package com.gmail.tatsukimatsumo.imagelab.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photocontentsresolver.PhotoContentsProviderClientRepository
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase.PhotoDatabaseRepository
import com.gmail.tatsukimatsumo.imagelab.model.imageloder.ContentsProviderImageLoader
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.PhotoIndexEntity
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey
import com.gmail.tatsukimatsumo.imagelab.model.usecase.PhotoUseCase
import kotlinx.coroutines.launch

class PhotoListViewModel(application: Application) : AndroidViewModel(application) {
    data class Photo(
        val url: Uri,
        val norm: Int
    ) {
        constructor(photoIndexEntity: PhotoIndexEntity) : this(
            photoIndexEntity.uri,
            photoIndexEntity.norm
        )
    }

    private val _photoList = MutableLiveData<List<Photo>>().also { it.value = emptyList() }
    val photoList: LiveData<List<Photo>> = _photoList

    val loadProgress: LiveData<Int>

    private val useCase: PhotoUseCase

    init {
        val repository = PhotoContentsProviderClientRepository(application.applicationContext)
        val indexRepository = PhotoDatabaseRepository(application.applicationContext)
        val imageLoader = ContentsProviderImageLoader(application.applicationContext)
        useCase = PhotoUseCase(repository, indexRepository, imageLoader)
        loadProgress = useCase.progress
    }

    fun onCreate() = reflectToViewSortBy()

    fun onTapCreateImageIndex() {
        viewModelScope.launch {
            useCase.deleteImageIndex()
            useCase.refreshImageDatabase()
            _photoList.value = getPhotos()
        }
    }

    fun sortByDateAdded() = reflectToViewSortBy(SortKey.SORT_KEY_DATE_ADDED)
    fun sortByDateAddedDesc() = reflectToViewSortBy(SortKey.SORT_KEY_DATE_ADDED_DESC)
    fun sortByNorm () = reflectToViewSortBy(SortKey.SORT_KEY_NORM)

    fun reflectToViewSortBy(sortKey: SortKey = SortKey.SORT_KEY_NONE) {
        viewModelScope.launch {
            _photoList.value = getPhotos(sortKey)
        }
    }
    private suspend fun getPhotos(sortKey: SortKey = SortKey.SORT_KEY_NONE) = useCase.getPhotos(sortKey).map { Photo(it) }
}