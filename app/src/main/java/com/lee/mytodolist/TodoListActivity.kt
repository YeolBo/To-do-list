package com.lee.mytodolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.RecyclerViewAdapter.RVTodoListAdapter
import com.lee.mytodolist.Utils.Constants.TAG
import com.lee.mytodolist.ViewModel.TodoListViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_to_do_list.*
import kotlinx.coroutines.launch

class TodoListActivity : AppCompatActivity(), RVInterface {

    // 뷰모델 가져오기
    private val todosViewModel = TodoListViewModel()

    // 어답터로 넘겨줄 데이터를 담을 리스트
    private var todoList = ArrayList<Todo>()

    // 어답터 생성
    private lateinit var rvTodoListAdapter: RVTodoListAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_to_do_list)

        // title 변경
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(true) // 타이틀 보이게 설정
            this.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
            title = "목 록"
        }

        // 리사이클러뷰 설정
        this.rvTodoListAdapter = RVTodoListAdapter(this)

        // 리사이클러뷰 레이아웃 설정
        val layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        my_todo_recycler_view.layoutManager = layoutManager

        // 위에서 생성한 어답터를 설정한 레이아웃에 장착
        my_todo_recycler_view.adapter = this.rvTodoListAdapter

        // 스크롤을 감지해서 페이지 추가
        my_todo_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.d(TAG, "onScrolled / dy: $dy")

                // progressbar 확인을 위한 딜레이 추가
                if (layoutManager.findLastCompletelyVisibleItemPosition() == todoList.size - 1) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        todosViewModel.pageCountUp()
                    }, 2000)
                }
            }
        })

        lifecycleScope.launch {
            // started 라이프사이클을 타면 자동으로 반복해서 구독처리 설정
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 각각 launch 스코프를 동해 각각 flow 구독 처리
                launch {
                    todosViewModel.currentPage.collect { changedPage ->
                        Log.d(TAG, "확인 / $changedPage")
                    }
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    launch {
                        todosViewModel.todos.collect { todoList ->
                            Log.d(TAG, "확인 onCreate: todoList.count(): ${todoList.count()}")
                            this@TodoListActivity.todoList = ArrayList(todoList)
                            // 어답터에 리스트 보냄
                            this@TodoListActivity.rvTodoListAdapter.submitList(ArrayList(todoList))
                            // 어답터에게 데이터가 변경되었다고 알림
                            this@TodoListActivity.rvTodoListAdapter.notifyDataSetChanged()
                        }
                    }
                }, 1000)

                launch {
                    todosViewModel.currentTodosCount.collect {
                        supportActionBar?.title = "목 록 : ${it}개"
                    }
                }

                launch {
                    todosViewModel.isLoading.collect {
                        if (it) {
                            todo_loading_progress_bar.visibility = View.VISIBLE
                        } else {
                            todo_loading_progress_bar.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }

    // 홈으로
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}