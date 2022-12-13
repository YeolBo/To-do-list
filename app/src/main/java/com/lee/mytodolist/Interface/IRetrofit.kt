package com.lee.mytodolist.Interface

import com.lee.mytodolist.Model.DeletedTodoResponse
import com.lee.mytodolist.Model.TodosResponse
import com.lee.mytodolist.Utils.TODO_API
import retrofit2.Call
import retrofit2.http.*

interface IRetrofit {

    // 모든 할 일 목록을 가져온다.
    @GET(TODO_API.BASE_URL + "todos")
    fun fetchTodos(
        @Query("page") page: Int,
        @Query("per_page") perPage: Int = 20
    ): Call<TodosResponse>

//    // 할일을 추가하고 추가된 할일을 반환한다.
//    @POST(TODO_API.BASE_URL + "todos")
//    fun sendText(@Body title: Todo): Call<TodosResponse>

    // 할일을 추가하고 추가된 할일을 반환한다.
    @Multipart
    @POST(TODO_API.BASE_URL + "todos")
    fun sendTextMultiPart(
        @Part("title") title: String,
        @Part("is_done") isDone: Boolean = false
    ): Call<TodosResponse>

    // 기존 할일을 수정하고 수정된 포스팅을 반환한다.
    @FormUrlEncoded
    @PUT(TODO_API.BASE_URL + "todos/{id}")
    fun reviseList(
        @Path("id") id: Int,
        @Field("title") title: String
    ): Call<DeletedTodoResponse>

    // 기존 할일을 수정하고 수정된 포스팅을 반환한다.
    @DELETE(TODO_API.BASE_URL + "todos/{todoId}")
    fun deletedList(@Path("todoId") id: Int): Call<DeletedTodoResponse>
}
















