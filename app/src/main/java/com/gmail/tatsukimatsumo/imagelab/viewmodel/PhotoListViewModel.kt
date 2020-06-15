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
        constructor(photoIndexEntity: PhotoIndexEntity) : this(photoIndexEntity.uri, photoIndexEntity.norm)
    }

    val photoList: MutableLiveData<List<Photo>> by lazy {
        MutableLiveData<List<Photo>>().also { it.value = emptyList() }
    }

    val loadProgress: LiveData<Int>

    private val repository = PhotoContentsProviderClientRepository(application.applicationContext)
    private val indexRepository = PhotoDatabaseRepository(application.applicationContext)
    private val imageLoader = ContentsProviderImageLoader(application.applicationContext)
    private val useCase = PhotoUseCase(repository, indexRepository, imageLoader)

    init {
        loadProgress = useCase.progress
    }

    fun onCreate() {
        viewModelScope.launch {
            photoList.value = getPhotos()
        }
    }

    fun onTapCreateImageIndex() {
        viewModelScope.launch {
            useCase.deleteImageIndex()
            useCase.refreshImageDatabase()
            photoList.value = getPhotos()
        }
    }

    fun sortByDateAdded() {
        viewModelScope.launch {
            photoList.value = getPhotos(SortKey.SORT_KEY_DATE_ADDED)
        }
    }

    fun sortByDateAddedDesc() {
        viewModelScope.launch {
            photoList.value = getPhotos(SortKey.SORT_KEY_DATE_ADDED_DESC)
        }
    }

    fun sortByNorm () {
        viewModelScope.launch {
            photoList.value = getPhotos(SortKey.SORT_KEY_NORM)
        }
    }

    private suspend fun getPhotos(sortKey: SortKey = SortKey.SORT_KEY_NONE) = useCase.getPhotos(sortKey).map { Photo(it) }
}