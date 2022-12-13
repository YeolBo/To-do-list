package com.lee.mytodolist.Utils

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

// 문자열이 json 형태인지
fun String?.isJsonObject(): Boolean {
    return this?.startsWith("{") == true && this.endsWith("}")
}

// 문자열이 json 형태인지
fun String?.isJsonArray(): Boolean {
    return this?.startsWith("[") == true && this.endsWith("]")
}