package com.techmaster.acharyaprashantjicodingtest.viewmodels

import Root
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmaster.acharyaprashantjicodingtest.repository.PhotosRepository
import com.techmaster.acharyaprashantjicodingtest.utility.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(private val photosRepository: PhotosRepository) : ViewModel() {
    val photosResponseLiveData: LiveData<NetworkResult<Root>>
    get() = photosRepository.photosResponseLiveData
    private var currentPage = 1
    fun getPhotos() {
        viewModelScope.launch(Dispatchers.IO) {

            photosRepository.getPhotos(currentPage)
            currentPage++
        }
    }
}
