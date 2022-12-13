package com.lee.mytodolist.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Edit
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.R
import com.lee.mytodolist.RecyclerViewHolder.RVEditTextHolder
import com.lee.mytodolist.RecyclerViewHolder.RVTodoListHolder

class RVTodoListAdapter(
    val rvInterface: RVInterface,
) : RecyclerView.Adapter<RVTodoListHolder>() {

    // 리사이클러뷰에서 보여줄 아이템을 담을 리스트
    private var todoList = ArrayList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVTodoListHolder {
        return RVTodoListHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.todo_list_item, parent, false),
            rvInterface,
        )
    }

    override fun onBindViewHolder(holder: RVTodoListHolder, position: Int) {
        holder.bindWithView(this.todoList[position])
    }

    // 보여줄 리스트 수
    override fun getItemCount(): Int {
        return this.todoList.size
    }

    // 외부에서 어답터에 데이터 배열을 넣어준다.
    fun submitList(todoList: ArrayList<Todo>) {
        this.todoList = todoList
    }
}