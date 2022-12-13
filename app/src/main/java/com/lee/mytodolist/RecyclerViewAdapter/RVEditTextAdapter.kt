package com.lee.mytodolist.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Edit
import com.lee.mytodolist.R
import com.lee.mytodolist.RecyclerViewHolder.RVEditTextHolder

class RVEditTextAdapter(val rvInterface: RVInterface) : RecyclerView.Adapter<RVEditTextHolder>() {

    // 리사이클러뷰에서 보여줄 아이템을 담을 리스트
    private var todoList = ArrayList<Edit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVEditTextHolder {
        return RVEditTextHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.edit_text_item, parent, false),
            rvInterface
        )
    }

    override fun onBindViewHolder(holder: RVEditTextHolder, position: Int) {
        holder.bindWithView(this.todoList[position])
    }

    // 보여줄 리스트 수
    override fun getItemCount(): Int {
        return this.todoList.size
    }

    // 외부에서 어답터에 데이터 배열을 넣어준다.
    fun submitList(todoList: ArrayList<Edit>) {
        this.todoList = todoList
    }
}