package com.lee.mytodolist.Model

import com.google.gson.annotations.SerializedName

data class Todo(
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("id")
    val id: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("is_done")
    val isDone: Boolean? = null,
    @SerializedName("updated_at")
    val updatedAt: String? = null
)