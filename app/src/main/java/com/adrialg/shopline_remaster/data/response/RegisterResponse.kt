package com.adrialg.shopline_remaster.data.response

import com.adrialg.shopline_remaster.data.User
import com.crocodic.core.api.ModelResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse (
    @SerializedName("data")
    @Expose
    val user: User?
): ModelResponse()