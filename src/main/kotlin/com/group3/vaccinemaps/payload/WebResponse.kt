package com.group3.vaccinemaps.payload

import org.springframework.http.ResponseEntity

typealias WebResponse<T> = ResponseEntity<Payload<T>>