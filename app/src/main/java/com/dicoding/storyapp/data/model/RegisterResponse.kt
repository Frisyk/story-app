package com.dicoding.storyapp.data.model

import com.google.gson.annotations.SerializedName
import retrofit2.http.Field

data class PostResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class RegisterRequest (
	@Field("name")
	val name: String,
	@Field("email")
	val email: String,
	@Field("password")
	val password: String
)
