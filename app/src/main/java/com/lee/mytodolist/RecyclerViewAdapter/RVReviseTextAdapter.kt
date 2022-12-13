package com.lee.mytodolist.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.R
import com.lee.mytodolist.RecyclerViewHolder.RVReviseTextHolder

class RVReviseTextAdapter(
    var rvInterface: RVInterface, var onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<RVReviseTextHolder>() {

    // 리사이클러뷰에서 보여줄 아이템을 담을 리스트
    private var todoList = ArrayList<Todo>()

    // 인터페이스와 레이아웃 장착
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVReviseTextHolder {
        return RVReviseTextHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.revise_list_item, parent, false),
            rvInterface,
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: RVReviseTextHolder, position: Int) {
        holder.bindWithView(this.todoList[position])
    }

    // 보여줄 아이탬 수
    override fun getItemCount(): Int {
        return this.todoList.size
    }

    // 외부에서 어답터에 데이터 배열을 넣어준다.
    fun submitList(todoList: ArrayList<Todo>) {
        this.todoList = todoList
    }
}