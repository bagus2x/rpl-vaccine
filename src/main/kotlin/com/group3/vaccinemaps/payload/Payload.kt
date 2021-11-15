package com.group3.vaccinemaps.payload

data class Payload<out T>(
    val code: Int,
    val status: String ,
    val data: T?
)