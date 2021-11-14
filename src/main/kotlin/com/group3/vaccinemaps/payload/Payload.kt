package com.group3.vaccinemaps.payload

data class Payload<out T>(
    val code: Int = 200,
    val status: String = "Ok",
    val data: T
)