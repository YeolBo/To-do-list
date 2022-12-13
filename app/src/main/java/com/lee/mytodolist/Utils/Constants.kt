package com.lee.mytodolist.Utils

object Constants {
    const val TAG: String = "로그"
}

// api 호출 상태를 위한 이넘 클래스
enum class RESPONSE_STATUS {
    OKAY,
    FAIL
}

// 싱글턴으로 만들어준다.
object TODO_API {
    const val BASE_URL: String = "히힣"
}