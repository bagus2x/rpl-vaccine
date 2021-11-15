package com.group3.vaccinemaps.controller

import com.group3.vaccinemaps.exception.BadRequestException
import com.group3.vaccinemaps.exception.ConflictException
import com.group3.vaccinemaps.exception.NotFoundException
import com.group3.vaccinemaps.exception.UnprocessableException
import com.group3.vaccinemaps.payload.WebResponse
import com.group3.vaccinemaps.payload.response
import com.group3.vaccinemaps.utils.logger
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.server.MethodNotAllowedException
import org.springframework.web.servlet.NoHandlerFoundException
import java.security.SignatureException
import javax.validation.ConstraintViolationException

@RestControllerAdvice
class ErrorController {
    val log = logger()

    @ExceptionHandler(
        value = [
            NoHandlerFoundException::class,
            MethodNotAllowedException::class,
            HttpRequestMethodNotSupportedException::class,
            NotFoundException::class
        ]
    )
    fun noHandler(e: Exception): WebResponse<String> {
        return response(404, "Not Found", e.message ?: "Not Found")
    }

    @ExceptionHandler(BadRequestException::class)
    fun badRequest(e: BadRequestException): WebResponse<String> {
        return response(400, "Bad Request", e.message ?: "Bad request")
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun jsonFormat(e: HttpMessageNotReadableException): WebResponse<String> {
        return response(400, "Bad Request", "Invalid json format")
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun constraintViolation(e: ConstraintViolationException): WebResponse<String> {
        return response(400, "Bad Request", e.message ?: "Constraint violation")
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun typeMismatch(e: MethodArgumentTypeMismatchException): WebResponse<String> {
        return response(400, "Bad Request", "Can not use string as number")
    }

    @ExceptionHandler(AuthenticationException::class)
    fun authError(e: AuthenticationException): WebResponse<String> {
        return response(401, "Unauthorized", e.message ?: "Unauthorized")
    }

    @ExceptionHandler(ConflictException::class)
    fun conflict(e: ConflictException): WebResponse<String> {
        return response(409, "Conflict", e.message ?: "Conflict")
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun invalidJwtToken(e: MalformedJwtException): WebResponse<String> {
        return response(400, "Bad Request", "Invalid jwt token")
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun invalidJwtToken(e: ExpiredJwtException): WebResponse<String> {
        return response(400, "Bad Request", "Jwt token is expired")
    }

    @ExceptionHandler(UnsupportedJwtException::class)
    fun invalidJwtToken(e: UnsupportedJwtException): WebResponse<String> {
        return response(400, "Bad Request", "JWT token is unsupported")
    }

    @ExceptionHandler(UnprocessableException::class)
    fun unprocessable(e: UnprocessableException): WebResponse<String> {
        return response(400, "Bad Request", e.message ?: "Unprocessable entity")
    }

    @ExceptionHandler(SignatureException::class)
    fun signature(e: SignatureException): WebResponse<String> {
        return response(500, "Internal Server Error", e.message ?: "")
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun signature(e: IllegalArgumentException): WebResponse<String> {
        return response(400, "Bad Request", e.message ?: "")
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun exception(e: AccessDeniedException): WebResponse<String> {
        return response(403, "Forbidden", e.message ?: "")
    }

    @ExceptionHandler(Exception::class)
    fun exception(e: Exception): WebResponse<String> {
        e.printStackTrace()
        return response(520, "Web Server Returned an Unknown Error", e.message ?: "")
    }
}