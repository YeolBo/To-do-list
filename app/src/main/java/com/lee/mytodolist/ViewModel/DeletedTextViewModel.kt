package com.lee.mytodolist.ViewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Retrofit.RetrofitManager
import com.lee.mytodolist.Utils.Constants
import com.lee.mytodolist.Utils.Constants.TAG
import com.lee.mytodolist.Utils.RESPONSE_STATUS.OKAY
import com.lee.mytodolist.Utils.RESPONSE_STATUS.FAIL
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DeletedTextViewModel : ViewModel() {

    // UI가 상태 업데이트를 가져옴
    // 데이터 리스트
    private val _todos = MutableStateFlow<List<Todo>>(emptyList())
    val todos: StateFlow<List<Todo>> = _todos

    // 현재 페이지
    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    // 삭제된 것
    private val _deletedTodo = MutableSharedFlow<Todo>()
    val deletedTodo: SharedFlow<Todo> = _deletedTodo

    // 목록 수
    val currentTodosCount: Flow<Int> = _todos.map { it.size }

    // 로딩 여부
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // 기존 플로우를 변형해서 다른 플로우로 만들기
    val deleteSuccessMsgEvent: Flow<String> = _deletedTodo.map { todo ->
        val todoId = todo.id ?: 0
        return@map "삭제완료 - id:$todoId"
    }

    init {
        viewModelScope.launch {
            _currentPage.collect() { changedPage ->
                Log.d(TAG, "DeletedTextViewModel - - changedPage : $changedPage\"")
                // 목록을 불러오기 위한 api 호출 부분
                apiCall()
            }
        }
    }

    // 페이지수를 1씩 올려준다
    fun pageCountUp() {
        _currentPage.value = _currentPage.value + 1
    }

    // 1. 뷰홀더 삭제 버튼 클릭 Int
    // 2. 액티비티에서 이벤트 받음
    // 3. 뷰모델 삭제 메소드 호출
    // 4. 삭제 처리
    // 5. 삭제 완료된 것으로 기존 할일 리스트 변경 - 삭제된 녀석만 기존에서 빼기 - filter 연산자 사용
    // 6. 뷰모델에서는 이미 데이터리스트가 바인딩 되어 있어서
    // 7. 액티비티에서는 바로 변경

    // 삭제
    fun deleteATodo(id: Int) {
        Log.d(TAG, "deleteATodo: id: $id")

        // api 호출
        RetrofitManager.instance.deletedList(id) { responseStatus, deletedTodoResponse ->
            when (responseStatus) {
                OKAY -> {
                    Log.d(TAG, "deleteATodo api 호출 성공 : $responseStatus")
                    // 옵셔널 처리
                    deletedTodoResponse?.data?.let { deletedTodo ->
                        // 코루틴 스콥에서 처리 - 플로우 이벤트 에밋 때문
                        viewModelScope.launch {
                            // 삭제된거 필터링해서 데이터 변경
                            _todos.value = _todos.value.filter { it.id != deletedTodo.id }
//                            _todos = _todos.filter { it != deletedTodo.id }
                            // 삭제된 녀석 보내기
                            _deletedTodo.emit(deletedTodo)
                        }
                    }
                }
                FAIL -> {
                    Log.d(TAG, "deleteATodo api 호출 실패 : $responseStatus")
                }
            }
        }
    }

    // 목록을 불러오기 위한 api 호출
    private fun apiCall() {
        // 로딩중
        viewModelScope.launch {
            _isLoading.emit(true)
        }
        RetrofitManager.instance.fetchTodos(page = _currentPage.value) { responseStatus, todosResponse ->
            when (responseStatus) {
                OKAY -> {
                    Log.d("TodosViewModel", "api 호출 성공 : $responseStatus")

                    val todos = todosResponse?.data ?: emptyList()

                    _todos.value = _todos.value + todos
                }
                FAIL -> {
                    Log.d("TodosViewModel", "api 호출 실패 : $responseStatus")
                }
            }
            viewModelScope.launch {
                _isLoading.emit(false)
            }
        }
    }
}
