package com.lee.mytodolist.RecyclerViewAdapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.R
import com.lee.mytodolist.RecyclerViewHolder.RVDeletedTextHolder

class RVDeletedTextAdapter(
    var rvInterface: RVInterface, var onItemClicked: (Int) -> Unit
) : RecyclerView.Adapter<RVDeletedTextHolder>() {

    private var todoList = ArrayList<Todo>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RVDeletedTextHolder {
        return RVDeletedTextHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.deleted_text_item, parent, false),
            rvInterface,
            onItemClicked
        )
    }

    override fun onBindViewHolder(holder: RVDeletedTextHolder, position: Int) {
        holder.bindWithView(this.todoList[position])
    }

    override fun getItemCount(): Int {
        return this.todoList.size
    }

    fun submitList(todoList: ArrayList<Todo>) {
        this.todoList = todoList
    }
}
