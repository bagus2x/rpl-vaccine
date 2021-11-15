package com.group3.vaccinemaps.security.jwt

import com.group3.vaccinemaps.entity.User
import com.group3.vaccinemaps.security.service.UserDetailsImpl
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jws
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.nio.charset.StandardCharsets
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtUtils {
    @Value("\${vaccine.maps.jwt_secret}")
    private lateinit var jwtSecret: String

    @Value("\${vaccine.maps.jwt_expiration_time_ms}")
    private val jwtExpirationMs = 999999

    private fun getKey() = Keys.hmacShaKeyFor(jwtSecret.toByteArray(StandardCharsets.UTF_8))

    fun generateJwtToken(authentication: Authentication): String {
        val userPrincipal: UserDetailsImpl = authentication.principal as UserDetailsImpl

        return Jwts.builder()
            .claim("userId", userPrincipal.user.id)
            .setSubject(userPrincipal.user.email)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(getKey())
            .compact()
    }

    fun generateJwtToken(user: User): String {
        return Jwts.builder()
            .claim("userId", user.id)
            .setSubject(user.email)
            .setIssuedAt(Date())
            .setExpiration(Date(Date().time + jwtExpirationMs))
            .signWith(getKey())
            .compact()
    }

    fun validateJwtToken(authToken: String?): Jws<Claims> {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(authToken)
    }

    fun parseJwt(request: HttpServletRequest): String? {
        val headerAuth = request.getHeader("Authorization")

        return when (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            true -> headerAuth.substring(7, headerAuth.length)
            else -> null
        }
    }
}