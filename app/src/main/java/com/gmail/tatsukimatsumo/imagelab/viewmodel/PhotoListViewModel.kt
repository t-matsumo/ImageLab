package com.gmail.tatsukimatsumo.imagelab.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gmail.tatsukimatsumo.imagelab.model.datasource.PhotoContentsProviderClientRepository
import com.gmail.tatsukimatsumo.imagelab.model.datasource.photodatabase.PhotoDatabaseRepository
import com.gmail.tatsukimatsumo.imagelab.model.usecase.PhotoUseCase
import kotlinx.coroutines.launch

class PhotoListViewModel(application: Application) : AndroidViewModel(application) {
    data class Photo(val url: Uri)

    val photoList: MutableLiveData<List<Photo>> by lazy {
        MutableLiveData<List<Photo>>().also { it.value = emptyList() }
    }

    private val repository = PhotoContentsProviderClientRepository(application.applicationContext)
    private val indexRepository = PhotoDatabaseRepository(application.applicationContext)
    private val useCase = PhotoUseCase(repository, indexRepository)

    fun onCreate() {
        viewModelScope.launch {
            photoList.value = getPhotos()
        }
    }

    fun onTapCreateImageIndex() {
        viewModelScope.launch {
            useCase.refreshImageDatabase()
            photoList.value = getPhotos()
        }
    }

    fun onTapDeleteImageIndex() {
        viewModelScope.launch {
            useCase.deleteImageIndex()
            photoList.value = emptyList()
        }
    }

    private suspend fun getPhotos() = useCase.getPhotos().map { Photo(it.uri) }
}