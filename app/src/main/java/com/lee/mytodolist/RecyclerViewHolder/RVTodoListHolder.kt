package com.lee.mytodolist.RecyclerViewHolder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Utils.Constants.TAG
import kotlinx.android.synthetic.main.todo_list_item.view.*

class RVTodoListHolder(
    itemView: View,
    rvInterface: RVInterface,
) : RecyclerView.ViewHolder(itemView) {

    private val todoList = itemView.to_do_list

    private var rvInterface: RVInterface? = null

    init {
        this.rvInterface = rvInterface
    }

    // 뷰가 데이터에 묶이기 전에 보낼 데이터
    fun bindWithView(list: Todo) {
        todoList.text = list.title
    }
}