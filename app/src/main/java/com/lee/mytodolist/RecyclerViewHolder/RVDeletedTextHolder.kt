package com.lee.mytodolist.RecyclerViewHolder

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Todo
import com.lee.mytodolist.Utils.Constants
import com.lee.mytodolist.Utils.Constants.TAG
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.deleted_text_item.view.*
import kotlinx.android.synthetic.main.todo_list_item.view.*

class RVDeletedTextHolder(
    itemView: View,
    rvInterface: RVInterface,
    var onItemClicked: (Int) -> Unit
) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

    private val todoList = itemView.deleted_list

    private var todo: Todo? = null

    private var rvInterface: RVInterface? = null

    init {
        this.rvInterface = rvInterface
        this.itemView.deleted_btn.setOnClickListener(this)
    }

    fun bindWithView(todo: Todo) {
        todoList.text = todo.title
        this.todo = todo
    }

    override fun onClick(v: View?) {
        when (v) {
            this.itemView.deleted_btn -> {
                Log.d(TAG, "onClick: 삭제 클릭")

                todo?.id?.let {
                    onItemClicked(it)
                } ?: Log.d(TAG, " id 없음")
            }
        }
    }
}
