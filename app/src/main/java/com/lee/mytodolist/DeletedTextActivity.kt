package com.lee.mytodolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.RecyclerViewAdapter.RVDeletedTextAdapter
import com.lee.mytodolist.Utils.Constants
import com.lee.mytodolist.ViewModel.DeletedTextViewModel
import kotlinx.android.synthetic.main.activity_deleted_text.*
import kotlinx.android.synthetic.main.activity_revise_text.*
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DeletedTextActivity : AppCompatActivity(), RVInterface {

    // 뷰 모델 가져오기
    private var todosViewModel = DeletedTextViewModel()

    // 어답터로 보낼 리스트 생성
    private var todoList = ArrayList<Todo>()

    // 어답터 생성
    private lateinit var rvDeletedTextAdapter: RVDeletedTextAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_deleted_text)
        // title 변경
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(true) // 타이틀 보이게 설정
            this.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
            title = "삭 제"
        }

        // 리사이클러뷰 설정
        this.rvDeletedTextAdapter = RVDeletedTextAdapter(this,
            onItemClicked = {
                todosViewModel.deleteATodo(it)
            })

        // 리사이클러뷰 레이아웃 설정
        val layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        my_deleted_recycler_view.layoutManager = layoutManager
        // 위에서 생성한 어답터를 설정한 레이아웃에 장착
        my_deleted_recycler_view.adapter = this.rvDeletedTextAdapter

        // 스크롤을 감지해서 페이지 추가
        my_deleted_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                Log.d(Constants.TAG, "onScrolled / dy: $dy")

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
                        Log.d(Constants.TAG, "확인 / $changedPage")
                    }
                }

                launch {
                    // 구독
                    todosViewModel.todos.collect { todoList ->
                        Log.d(Constants.TAG, "확인 onCreate: todoList.count(): ${todoList.count()}")
                        this@DeletedTextActivity.todoList = ArrayList(todoList)
                        // 어답터에 리스트 보냄
                        this@DeletedTextActivity.rvDeletedTextAdapter.submitList(ArrayList(todoList))
                        this@DeletedTextActivity.rvDeletedTextAdapter.notifyDataSetChanged()
                    }
                }

                // 데이터는 삭제되었음.
                // 1. 뷰모델에서
                // 2. 서브밋 부분
                // 3.
//                launch {
//                    todosViewModel.deletedTodo.collect{ deletedTodo ->
//                        Log.d(Constants.TAG, "onCreate: ")
//                        deletedTodo.id?.let {
//                            Toast.makeText(this@DeletedTextActivity,
//                                "삭제 성공 ID: $it", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }

                launch {
                    todosViewModel.deleteSuccessMsgEvent.collect {
                        Toast.makeText(this@DeletedTextActivity, it, Toast.LENGTH_SHORT).show()
                    }
                }

                launch {
                    todosViewModel.currentTodosCount.collect {
                        supportActionBar?.title = "삭 제 : ${it}개"
                    }
                }

                launch {
                    todosViewModel.isLoading.collect {
                        if (it) {
                            deleted_loading_progress_bar.visibility = View.VISIBLE
                        } else {
                            deleted_loading_progress_bar.visibility = View.GONE
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
