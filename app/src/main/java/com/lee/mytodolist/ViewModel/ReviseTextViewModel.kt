package com.lee.mytodolist.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Retrofit.RetrofitManager
import com.lee.mytodolist.Utils.Constants.TAG
import com.lee.mytodolist.Utils.RESPONSE_STATUS.OKAY
import com.lee.mytodolist.Utils.RESPONSE_STATUS.FAIL
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ReviseTextViewModel : ViewModel() {

    // UI가 상태 업데이트를 가져옴
    // 데이터 리스트
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    // 현재 페이지
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    // 삭제된 것
    private val _reviseTodo = MutableSharedFlow<Todo>()
    val deletedTodo: SharedFlow<Todo> = _reviseTodo

    // 리로드
    val reloadAction = MutableSharedFlow<Unit>()

    // 수정
    val editAction = MutableSharedFlow<Todo>()

    // 목록 수
    val currentTodosCount: Flow<Int> = _todos.map { it.size }

    // 로딩 여부
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        viewModelScope.launch {

            launch {
                _currentPage.collect { changedPage ->
                    Log.d(TAG, "ReviseTextViewModel - changedPage : $changedPage")
                    // 목록을 불러오기 위한 api 호출 부분
                    apiCall()
                }
            }

            launch {
                reloadAction.collect {
                    Log.d(TAG, "ReviseTextViewModel - reloadAction")
                    refreshDataApiCall()
                }
            }

            launch {
                editAction.collect {
                    Log.d(TAG, "ReviseTextViewModel - editAction : it: ${it.id}")
                    updateATodo(it)
                }
            }
        }
    }

    // 페이지수를 1씩 올려준다
    fun pageCountUp() {
        _currentPage.value = _currentPage.value + 1
    }

    // 목록을 불러오기 위한 api 호출 부분
    private fun apiCall() {
        // 로딩중
        viewModelScope.launch {
            _isLoading.emit(true)
        }

        RetrofitManager.instance.fetchTodos(page = _currentPage.value) { responseStatus, todosResponse ->
            when (responseStatus) {
                OKAY -> {
                    Log.d(TAG, "TodosViewModel / api 호출 성공 : $responseStatus")

                    val todos = todosResponse?.data ?: emptyList()

                    _todos.value = _todos.value + todos
                }
                FAIL -> {
                    Log.d(TAG, "TodosViewModel / api 호출 실패 : $responseStatus")
                }
            }
            viewModelScope.launch {
                _isLoading.emit(false)
            }
        }
    }

    private fun refreshDataApiCall() {
        _currentPage.value = 1
        RetrofitManager.instance.fetchTodos(page = _currentPage.value) { responseStatus, todosResponse ->
            when (responseStatus) {
                OKAY -> {
                    Log.d(TAG, "TodosViewModel / api 호출 성공 : $responseStatus")

                    val todos = todosResponse?.data ?: emptyList()

                    _todos.value = todos
                }
                FAIL -> {
                    Log.d(TAG, "TodosViewModel / api 호출 실패 : $responseStatus")
                }
            }
        }
    }

    // 수정된 할일을 업데이트한다
    private fun updateATodo(editedTodo: Todo) {
        // 현재 수정된 할일의 아이디를 가진 녀석의 배열 인덱스를 찾는다
        // 수정된 할일을 그 위치에 넣는다/갈아끼운다
        // List<Todo>

        // 수정된 인덱스가 몇번째인가 검색
        val todoIndex = _todos.value.indexOfFirst {
            it.id == editedTodo.id
        }

        // 수정을 위해 담을 그릇
        val existingTodos = _todos.value.toMutableList()

        // 갈아끼움
        existingTodos[todoIndex] = editedTodo

        // 전체적으로 수정
        _todos.value = existingTodos
    }
}