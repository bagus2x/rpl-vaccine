package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.exception.ConflictException
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.payload.Payload
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.utils.logger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import java.security.SignatureException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ErrorController {
    val log = logger()

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(e: BadRequestException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", e.message ?: "Bad request"))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun jsonFormat(e: HttpMessageNotReadableException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", "Invalid json format"))
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolation(e: ConstraintViolationException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", e.message ?: "Constraint violation"))
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun typeMismatch(e: MethodArgumentTypeMismatchException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", "Can not use string as number"))
    }

    @ExceptionHandler(AuthenticationException::class)
    fun authError(e: AuthenticationException): WebResponse<String> {
        return WebResponse.status(401).body(Payload(401, "Unauthorized", e.message ?: "Unauthorized"))
    }

    @ExceptionHandler(NotFoundException::class)
    fun notFound(e: NotFoundException): WebResponse<String> {
        return WebResponse.status(404).body(Payload(404, "Not Found", e.message ?: "Not found"))
    }

    @ExceptionHandler(ConflictException::class)
    fun conflict(e: ConflictException): WebResponse<String> {
        return WebResponse.status(409).body(Payload(404, "Conflict", e.message ?: "Conflict"))
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun invalidJwtToken(e: MalformedJwtException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", "Invalid jwt token"))
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun invalidJwtToken(e: ExpiredJwtException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", "Jwt token is expired"))
    }

    @ExceptionHandler(UnsupportedJwtException::class)
    fun invalidJwtToken(e: UnsupportedJwtException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", "JWT token is unsupported"))
    }

    @ExceptionHandler(SignatureException::class)
    fun signature(e: SignatureException): WebResponse<String> {
        return WebResponse.status(500).body(Payload(500, "Internal Server Error", e.message ?: ""))
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun signature(e: IllegalArgumentException): WebResponse<String> {
        return WebResponse.status(400).body(Payload(400, "Bad Request", e.message ?: ""))
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun exception(e: AccessDeniedException): WebResponse<String> {
        return WebResponse.status(403).body(Payload(403, "Forbidden", e.message ?: ""))
    }

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception): WebResponse<String> {
        log.error("Exception error: ${e.message} $e")
        return WebResponse.status(500).body(Payload(500, "Internal Server Error", e.message ?: ""))
    }
}