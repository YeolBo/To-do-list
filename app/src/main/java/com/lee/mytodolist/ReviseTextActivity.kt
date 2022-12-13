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
import com.lee.mytodolist.Dialog.CustomDialog
import com.lee.mytodolist.Interface.IDialog
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.RecyclerViewAdapter.RVReviseTextAdapter
import com.lee.mytodolist.Retrofit.RetrofitManager
import com.lee.mytodolist.Utils.Constants.TAG
import com.lee.mytodolist.Utils.RESPONSE_STATUS
import com.lee.mytodolist.ViewModel.ReviseTextViewModel
import kotlinx.android.synthetic.main.activity_edit_text.*
import kotlinx.android.synthetic.main.activity_revise_text.*
import kotlinx.android.synthetic.main.activity_to_do_list.*
import kotlinx.android.synthetic.main.customdialog.*
import kotlinx.android.synthetic.main.revise_list_item.*
import kotlinx.coroutines.launch

class ReviseTextActivity : AppCompatActivity(), RVInterface, IDialog {

    // 뷰모델 가져오기
    private var todosViewModel = ReviseTextViewModel()

    // 어답터로 넘겨줄 데이터를 받을 리스트
    private var todoList = ArrayList<Todo>()

    // 어답터 생성
    private lateinit var rvReviseTextAdapter: RVReviseTextAdapter

    // 커스텀 다이얼 로그 생성
    private var editDialog: CustomDialog? = null

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_revise_text)
        // title 변경
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(true) // 타이틀 보이게 설정
            this.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
            title = "수 정"
        }

        // 리사이클러뷰 설정
        this.rvReviseTextAdapter = RVReviseTextAdapter(this,
            onItemClicked = {
                // 수정 다이얼로그
                editDialog = CustomDialog(this, this, it)
                editDialog?.show()
            })

        // 리사이클러뷰 레이아웃 설정
        val layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        my_revise_recycler_view.layoutManager = layoutManager
        // 위에서 생성한 어답터를 설정한 레이아웃에 장착
        my_revise_recycler_view.adapter = this.rvReviseTextAdapter

        // 스크롤을 감지해서 페이지 추가
        my_revise_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        Log.d(TAG, "changedPage / $changedPage")
                    }
                }

                launch {
                    todosViewModel.todos.collect { todoList ->
                        Log.d(TAG, "onCreate: todoList.count(): ${todoList.count()}")
                        this@ReviseTextActivity.todoList = ArrayList(todoList)
                        // 어답터에 리스트 보냄
                        this@ReviseTextActivity.rvReviseTextAdapter.submitList(ArrayList(todoList))
                        this@ReviseTextActivity.rvReviseTextAdapter.notifyDataSetChanged()
                    }
                }

                launch {
                    todosViewModel.currentTodosCount.collect {
                        supportActionBar?.title = "수 정 : ${it}개"
                    }
                }

                launch {
                    todosViewModel.isLoading.collect {
                        if (it) {
                            revise_loading_progress_bar.visibility = View.VISIBLE
                        } else {
                            revise_loading_progress_bar.visibility = View.GONE
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

    // 수정 입력 버튼
    override fun inputBtn(id: Int, userInput: String) {
        if (userInput.length < 6) {
            Toast.makeText(this, "6글자 이상 입력해 주세요!", Toast.LENGTH_SHORT).show()
        }
        Log.d(TAG, "inputBtn: userInput: $userInput")

        RetrofitManager.instance.reviseList(id, userInput) { responseStatus, deletedTodoResponse ->
            when (responseStatus) {
                RESPONSE_STATUS.OKAY -> {
                    lifecycleScope.launch {
                        deletedTodoResponse?.data?.let {
                            todosViewModel.editAction.emit(it)
                        }
                    }
                }
                RESPONSE_STATUS.FAIL -> {
                    Log.d(TAG, "TodosViewModel / api 호출 실패 : $responseStatus")
                }
            }
            editDialog?.dismiss()
        }
    }
}