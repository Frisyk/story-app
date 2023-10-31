package com.dicoding.storyapp.view.upload

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.model.PostResponse
import com.dicoding.storyapp.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException

class UploadViewModel(private val repository: UserRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _response = MutableLiveData<PostResponse>()
    val response: LiveData<PostResponse> = _response

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun postStory(multipartBody: MultipartBody.Part, requestBody: RequestBody) {
        _isLoading.value = false
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val successResponse = repository.postStory(multipartBody, requestBody)
                _response.value = successResponse
            } catch (e: HttpException) {
                val errorBody = e.response()?.errorBody()?.string()
                val errorResponse = Gson().fromJson(errorBody, PostResponse::class.java)
                _response.value = errorResponse
                _isLoading.value = false
            }
        }
    }
}