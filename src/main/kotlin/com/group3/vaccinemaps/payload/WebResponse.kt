package com.group3.vaccinemaps.payload

import org.springframework.http.ResponseEntity

typealias WebResponse<T> = ResponseEntity<Payload<T>>

fun <T> response(code: Int = 200, status: String, data: T): WebResponse<T> {
    return WebResponse.status(code).body(Payload(code, status, data))
}