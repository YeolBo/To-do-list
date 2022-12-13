package com.lee.mytodolist.Dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import com.lee.mytodolist.Interface.IDialog
import com.lee.mytodolist.R
import com.lee.mytodolist.Utils.Constants.TAG
import kotlinx.android.synthetic.main.customdialog.*

class CustomDialog(
    context: Context,
    iDialog: IDialog,
    var id: Int
) : Dialog(context), View.OnClickListener {

    private var iDialog: IDialog? = null

    init {
        this.iDialog = iDialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customdialog)

        window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog_input_btn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            dialog_input_btn -> {
                Log.d(TAG, "CustomDialog - dialog_input_btn 클릭")

                val userInput = dialog_edit_text.text.toString()
                this.iDialog?.inputBtn(id, userInput)
            }
        }
    }
}