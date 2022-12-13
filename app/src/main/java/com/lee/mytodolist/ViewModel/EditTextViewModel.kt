package com.lee.mytodolist.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.mytodolist.Model.Todo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class EditTextViewModel : ViewModel() {

    // 데이터 리스트
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())

    // UI가 상태 업데이트를 가져옴
    val todos: StateFlow<List<Todo>> = _todos

    init {
        viewModelScope.launch {
        }
    }

}