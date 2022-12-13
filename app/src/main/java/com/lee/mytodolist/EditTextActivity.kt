package com.lee.mytodolist

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Edit
import com.lee.mytodolist.RecyclerViewAdapter.RVEditTextAdapter
import com.lee.mytodolist.Retrofit.RetrofitManager
import com.lee.mytodolist.Utils.Constants
import com.lee.mytodolist.Utils.RESPONSE_STATUS.OKAY
import com.lee.mytodolist.Utils.RESPONSE_STATUS.FAIL
import com.lee.mytodolist.ViewModel.EditTextViewModel
import kotlinx.android.synthetic.main.activity_edit_text.*
import kotlinx.coroutines.launch

class EditTextActivity : AppCompatActivity(), RVInterface {

    // 뷰모델 가져오기
    private val editTextViewModel = EditTextViewModel()

    // 어답터로 넘겨줄 데이터를 담을 리스트
    private var todoList = ArrayList<Edit>()

    // 어답터 생성
    private lateinit var rvEditTextAdapter: RVEditTextAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_text)

        // title 변경
        supportActionBar?.apply {
            this.setDisplayShowTitleEnabled(true) // 타이틀 보이게 설정
            this.setDisplayHomeAsUpEnabled(true) // 뒤로가기 버튼
            title = "작 성"
        }

        // 리사이클러뷰 설정
        this.rvEditTextAdapter = RVEditTextAdapter(this)
        // 어답터에 리스트 보냄
        this.rvEditTextAdapter.submitList(todoList)
        // 리사이클러뷰 레이아웃 설정
        my_edit_recycler_view.layoutManager = GridLayoutManager(
            this,
            1,
            GridLayoutManager.VERTICAL,
            false
        )
        // 위에서 생성한 어답터를 설정한 레이아웃에 장착
        my_edit_recycler_view.adapter = this.rvEditTextAdapter

        lifecycleScope.launch {
            // started 라이프사이클을 타면 자동으로 반복해서 구독처리 설정
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                // 각각 launch 스코프를 동해 각각 flow 구독 처리

                launch {
                    editTextViewModel.todos.collect { todoList ->
                        Log.d(Constants.TAG, "확인 onCreate: todoList.count(): ${todoList.count()}")

                        // 입력 버튼 클릭시
                        text_input_btn.setOnClickListener {

                            if (my_edit_text.length() >= 6) {
                                // api 호출
                                apiCall()

                                // 입력한 글을 todoList 에 추가해준다
                                val userInput = my_edit_text.text.toString()
                                this@EditTextActivity.todoList.add(Edit(userInput))

                                // 리사이클러뷰 어답터에 데이터가 추가되었다고 알려준다.
                                rvEditTextAdapter.notifyDataSetChanged()

                                // 입력 후 텍스트창 초기화
                                my_edit_text.setText("")
                                Toast.makeText(
                                    this@EditTextActivity,
                                    "할일이 성공적으로 추가되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(
                                    this@EditTextActivity,
                                    "6글자 이상 입력해주세요!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
    }

    // api 호출 부분
    private fun apiCall() {
        RetrofitManager.instance.addTodos(title = my_edit_text.text.toString()) { responseStatus, _ ->
            when (responseStatus) {
                OKAY -> {
                    Log.d("TodosViewModel", "api 호출 성공 : $responseStatus")
                }
                FAIL -> {
                    Log.d("TodosViewModel", "api 호출 실패 : $responseStatus")
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