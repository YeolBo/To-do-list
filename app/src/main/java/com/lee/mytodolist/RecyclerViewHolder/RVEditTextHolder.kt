package com.lee.mytodolist.RecyclerViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.lee.mytodolist.Interface.RVInterface
import com.lee.mytodolist.Model.Edit
import kotlinx.android.synthetic.main.edit_text_item.view.*

class RVEditTextHolder(
    itemView: View,
    rvInterface: RVInterface
) : RecyclerView.ViewHolder(itemView) {

    private val inputText = itemView.to_do_text

    private var iEditText: RVInterface? = null

    init {
        this.iEditText = rvInterface
    }

    // 뷰가 데이터에 묶이기 전에 보낼 데이터
    fun bindWithView(Text: Edit) {
        inputText.text = Text.editText
    }
}