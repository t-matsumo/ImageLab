package com.gmail.tatsukimatsumo.imagelab.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photocontentsresolver.PhotoContentsProviderClientRepository
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase.Photo
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase.PhotoDatabaseRepository
import com.gmail.tatsukimatsumo.imagelab.model.imageloder.ContentsProviderImageLoader
import com.gmail.tatsukimatsumo.imagelab.model.repository.PhotoIndexRepository.SortKey
import com.gmail.tatsukimatsumo.imagelab.model.usecase.PhotoUseCase
import kotlinx.coroutines.launch

class PhotoListViewModel(application: Application) : AndroidViewModel(application) {
    val photoList: LiveData<List<Photo>>
    private val sortKey: MutableLiveData<SortKey> by lazy {
        MutableLiveData<SortKey>().also { it.value = SortKey.SORT_KEY_NONE }
    }

    private val useCase: PhotoUseCase

    init {
        val repository = PhotoContentsProviderClientRepository(application.applicationContext)
        val indexRepository = PhotoDatabaseRepository(application.applicationContext)
        val imageLoader = ContentsProviderImageLoader(application.applicationContext)
        useCase = PhotoUseCase(repository, indexRepository, imageLoader)

        photoList = Transformations.switchMap(sortKey, useCase::getPhotosLiveDataSortedBy)
    }

    fun onCreate() {
        viewModelScope.launch {
            useCase.refreshImageDatabase()
        }
    }

    fun onTapDeleteImageIndex() {
        viewModelScope.launch {
            useCase.deleteImageIndex()
        }
    }

    fun onTapRefreshImageIndex() {
         viewModelScope.launch {
            useCase.refreshImageDatabase()
        }
    }

    fun sortByDateAdded() = reflectToViewSortBy(SortKey.SORT_KEY_DATE_ADDED)
    fun sortByDateAddedDesc() = reflectToViewSortBy(SortKey.SORT_KEY_DATE_ADDED_DESC)
    fun sortByNorm() = reflectToViewSortBy(SortKey.SORT_KEY_NORM)

    private fun reflectToViewSortBy(sortKey: SortKey = SortKey.SORT_KEY_NONE) {
        this.sortKey.value = sortKey
    }
}