package com.example.focusmode.models

data class AppBlockInfo(
    val name: String,
    val packageName: String,
    var isBlocked: Boolean = false
)
