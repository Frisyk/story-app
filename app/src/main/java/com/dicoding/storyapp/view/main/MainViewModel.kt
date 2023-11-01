package com.dicoding.storyapp.view.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.storyapp.data.UserRepository
import com.dicoding.storyapp.data.model.ListStoryItem
import com.dicoding.storyapp.data.pref.UserModel
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getAllStory(token: String): LiveData<PagingData<ListStoryItem>>? {
        _isLoading.value = true
        return try {
            val response = repository.getStories(token).cachedIn(viewModelScope)
            _isLoading.value = false
            response
        } catch (e: HttpException) {
            _isLoading.value = false
            null
        }
    }

}
