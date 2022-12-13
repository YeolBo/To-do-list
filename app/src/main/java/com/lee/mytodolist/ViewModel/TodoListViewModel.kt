package com.lee.mytodolist.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Retrofit.RetrofitManager
import com.lee.mytodolist.Utils.Constants.TAG
import com.lee.mytodolist.Utils.RESPONSE_STATUS
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TodoListViewModel : ViewModel() {

    // 데이터 리스트
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // 현재 페이지
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    // UI가 상태 업데이트를 가져옴
    val todos: StateFlow<List<Todo>> = _todos

    val currentTodosCount: Flow<Int> = _todos.map { it.size }

    // 로딩 여부
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {
            _currentPage.collect { changedPage ->
                Log.d(TAG, "TodoListViewModel - changedPage : $changedPage")
                // api 호출
                apiCall()
            }
        }
    }

    // 페이지수를 1씩 올려준다
    fun pageCountUp() {
        _currentPage.value = _currentPage.value + 1
    }

    // api 호출 부분
    private fun apiCall() {

        // 로딩중
        viewModelScope.launch {
            _isLoading.emit(true)
        }

        RetrofitManager.instance.fetchTodos(page = _currentPage.value) { responseStatus, todosResponse ->
            when (responseStatus) {
                RESPONSE_STATUS.OKAY -> {
                    Log.d("TodosViewModel", "api 호출 성공 : $responseStatus")

                    val todos = todosResponse?.data ?: emptyList()

                    _todos.value = _todos.value + todos
                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d("TodosViewModel", "api 호출 실패 : $responseStatus")
                }
            }
            viewModelScope.launch {
                _isLoading.emit(false)
            }
        }
    }
}