package com.dicoding.storyapp.data



import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.storyapp.data.api.ApiService
import com.dicoding.storyapp.data.database.StoryDatabase
import com.dicoding.storyapp.data.model.ListStoryItem
import com.dicoding.storyapp.data.model.LoginRequest
import com.dicoding.storyapp.data.model.LoginResponse
import com.dicoding.storyapp.data.model.PostResponse
import com.dicoding.storyapp.data.model.RegisterRequest
import com.dicoding.storyapp.data.model.StoryResponse
import com.dicoding.storyapp.data.paging.StoryRemoteMediator
import com.dicoding.storyapp.data.pref.UserModel
import com.dicoding.storyapp.data.pref.UserPreference
import kotlinx.coroutines.flow.Flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

class UserRepository private constructor(
    private val userPreference: UserPreference,
    private val apiService: ApiService,
    private val storyDatabase: StoryDatabase
) {

    suspend fun saveSession(user: UserModel) {
        userPreference.saveSession(user)
        Log.d("session", user.toString())
    }

    fun getSession(): Flow<UserModel> {
        return userPreference.getSession()
    }

    suspend fun logout() {
        userPreference.logout()
    }

    suspend fun postRegister(user: RegisterRequest): PostResponse {
        return apiService.register(user)
    }

    suspend fun postLogin(user: LoginRequest): LoginResponse {
        return apiService.login(user)
    }

    suspend fun postStory(token: String, multipartBody: MultipartBody.Part, requestBody: RequestBody, location: Location?): PostResponse {
        if (location != null) {
            return apiService.uploadStory(
                token,
                multipartBody,
                requestBody,
                location.latitude.toString().toRequestBody("text/plain".toMediaType()),
                location.longitude.toString().toRequestBody("text/plain".toMediaType())
            )
        } else {
            return apiService.uploadStory(
                token,
                multipartBody,
                requestBody,
                null,
                null
            )
        }
    }


    suspend fun getLocation(token: String) : StoryResponse {
        return apiService.getStoriesWithLocation(token)
    }

    fun getStories(token: String): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
//                StoryPagingSource(token, apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    companion object {
        @Volatile
        private var instance: UserRepository? = null
        fun getInstance(
            userPreference: UserPreference,
            apiService: ApiService,
            storyDatabase: StoryDatabase
        ): UserRepository =
            instance ?: synchronized(this) {
                instance ?: UserRepository(userPreference, apiService, storyDatabase)
            }.also { instance = it }
    }
}