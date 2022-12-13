package com.lee.mytodolist.Model


import com.google.gson.annotations.SerializedName

data class DeletedTodoResponse(
    @SerializedName("data")
    val `data`: Todo?,
    @SerializedName("message")
    val message: String?,
)
