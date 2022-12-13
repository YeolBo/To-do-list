package com.lee.mytodolist.Retrofit

import android.annotation.SuppressLint
import android.util.Log
import com.lee.mytodolist.Interface.IRetrofit
import com.lee.mytodolist.Model.DeletedTodoResponse
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Model.TodosResponse
import com.lee.mytodolist.Utils.Constants.TAG
import com.lee.mytodolist.Utils.RESPONSE_STATUS
import com.lee.mytodolist.Utils.TODO_API
import retrofit2.Call
import retrofit2.Response

class RetrofitManager {

    // instance 에 RetrofitManager 을 담아준다.
    companion object {
        val instance = RetrofitManager()
    }

    // 레트로핏 인터페이스를 가져오면서 http 콜 만들기
    private val iRetrofit: IRetrofit? =
        RetrofitClient.getClient(
            TODO_API.BASE_URL,
            method = RetrofitClient.HTTP_METHOD.GET
        )?.create(IRetrofit::class.java)

    // 할일 목록 가져오기
    fun fetchTodos(page: Int, completion: (RESPONSE_STATUS, TodosResponse?) -> Unit) {
        // 언랩핑
        val call = iRetrofit?.fetchTodos(page) ?: return

        call.enqueue(object : retrofit2.Callback<TodosResponse> {
            // 응답 성공시 api 상태를 RESPONSE_STATUS.OKAY 로 보내고 response.body()를 보여준다.
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<TodosResponse>, response: Response<TodosResponse>) {
                Log.d(TAG, "응답 성공 - onResponse() called / response : ${response.raw()}")

                when (response.code()) {
                    200 -> completion(RESPONSE_STATUS.OKAY, response.body())
                }
            }

            // 응답 실패시 RESPONSE_STATUS.FAIL 로 보내준다.
            override fun onFailure(call: Call<TodosResponse>, t: Throwable) {
                Log.d(TAG, "응답 실패 - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }

    // 할일 목록 추가
    fun addTodos(title: String?, completion: (RESPONSE_STATUS, TodosResponse?) -> Unit) {
        // 언랩핑
        val term = title ?: return
        Todo(title = term)

        val iRetrofit: IRetrofit? =
            RetrofitClient.getClient(
                TODO_API.BASE_URL,
                method = RetrofitClient.HTTP_METHOD.POST
            )?.create(IRetrofit::class.java)

        val call = iRetrofit?.sendTextMultiPart(title) ?: return

        call.enqueue(object : retrofit2.Callback<TodosResponse> {
            // 응답 성공시 api 상태를 RESPONSE_STATUS.OKAY 로 보내고 response.body()를 보여준다.
            @SuppressLint("SimpleDateFormat")
            override fun onResponse(call: Call<TodosResponse>, response: Response<TodosResponse>) {
                Log.d(TAG, "응답 성공 - onResponse() called / response : ${response.raw()}")

                when (response.code()) {
                    // response.code 의 상태코드가 200이면
                    200 -> completion(RESPONSE_STATUS.OKAY, response.body())
                }
            }

            // 응답 실패시 RESPONSE_STATUS.FAIL 로 보내준다.
            override fun onFailure(call: Call<TodosResponse>, t: Throwable) {
                Log.d(TAG, "응답 실패 - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }

    // 할일 목록 수정
    fun reviseList(
        id: Int,
        title: String?,
        completion: (RESPONSE_STATUS, DeletedTodoResponse?) -> Unit
    ) {
        // 언랩핑
        title ?: return

        val iRetrofit: IRetrofit? =
            RetrofitClient.getClient(
                TODO_API.BASE_URL,
                method = RetrofitClient.HTTP_METHOD.PUT
            )?.create(IRetrofit::class.java)

        val call = iRetrofit?.reviseList(id, title) ?: return

        call.enqueue(object : retrofit2.Callback<DeletedTodoResponse> {
            override fun onResponse(
                call: Call<DeletedTodoResponse>,
                response: Response<DeletedTodoResponse>
            ) {
                Log.d(TAG, "응답 성공 - onResponse() called / response : ${response.raw()}")

                when (response.code()) {
                    200 -> completion(RESPONSE_STATUS.OKAY, response.body())
                }
            }

            override fun onFailure(call: Call<DeletedTodoResponse>, t: Throwable) {
                Log.d(TAG, "응답 실패 - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }
        })
    }

    // 할일 목록 삭제
    fun deletedList(id: Int, completion: (RESPONSE_STATUS, DeletedTodoResponse?) -> Unit) {

        val iRetrofit: IRetrofit? =
            RetrofitClient.getClient(
                TODO_API.BASE_URL,
                method = RetrofitClient.HTTP_METHOD.DELETE
            )?.create(IRetrofit::class.java)

        val call = iRetrofit?.deletedList(id) ?: return

        call.enqueue(object : retrofit2.Callback<DeletedTodoResponse> {
            override fun onResponse(
                call: Call<DeletedTodoResponse>,
                response: Response<DeletedTodoResponse>
            ) {
                Log.d(TAG, "응답 성공 - onResponse() called / response : ${response.raw()}")

                when (response.code()) {
                    200 -> completion(RESPONSE_STATUS.OKAY, response.body())
                }
            }

            override fun onFailure(call: Call<DeletedTodoResponse>, t: Throwable) {
                Log.d(TAG, "응답 실패 - onFailure() called / t : $t")
                completion(RESPONSE_STATUS.FAIL, null)
            }

        })
    }

}