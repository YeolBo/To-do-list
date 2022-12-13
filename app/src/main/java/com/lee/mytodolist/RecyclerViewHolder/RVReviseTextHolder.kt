package com.lee.mytodolist.RecyclerViewHolder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Utils.Constants
import com.lee.mytodolist.Utils.Constants.TAG
import kotlinx.android.synthetic.main.revise_list_item.view.*

class RVReviseTextHolder(
    itemView: View,
    rvInterface: RVInterface,
    var onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private var todoList = itemView.revise_to_do_list

    private var todo: Todo? = null

    // interface 연결
    private var rvInterface: RVInterface? = null

    init {
        this.rvInterface = rvInterface
        this.itemView.revise_btn.setOnClickListener(this)
    }

    // 뷰가 데이터에 묶이기 전에 보낼 데이터
    fun bindWithView(List: Todo) {
        todoList.text = List.title
        this.todo = List
//        Log.d(TAG, "bindWithView: List: $List")
    }

    override fun onClick(v: View?) {
        when (v) {
            this.itemView.revise_btn -> {
                Log.d(TAG, "onClick: 수정 클릭")

                todo?.id?.let {
                    onItemClicked(it)
                } ?: Log.d(TAG, " id 없음")
            }
        }
    }
}