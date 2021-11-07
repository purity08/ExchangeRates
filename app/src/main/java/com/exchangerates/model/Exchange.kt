package com.exchangerates.model

data class Exchange(
    val Cur_id: String,
    val Date: String,
    val Cur_Abbreviation: String,
    val Cur_Scale: String,
    val Cur_Name: String,
    val Cur_OfficialRate: String
) {
    var isShowing: Boolean = false
}
