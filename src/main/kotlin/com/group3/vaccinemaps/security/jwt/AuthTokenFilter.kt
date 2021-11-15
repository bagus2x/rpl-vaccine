package com.group3.vaccinemaps.security.jwt

import com.group3.vaccinemaps.security.service.UserDetailServiceImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.lang.NonNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import org.springframework.web.servlet.HandlerExceptionResolver
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthTokenFilter : OncePerRequestFilter() {
    @Autowired
    @Qualifier("handlerExceptionResolver")
    private lateinit var resolver: HandlerExceptionResolver

    @Autowired
    private lateinit var jwtUtils: JwtUtils

    @Autowired
    private lateinit var userDetailsService: UserDetailServiceImpl

    override fun doFilterInternal(
        @NonNull request: HttpServletRequest,
        @NonNull response: HttpServletResponse,
        @NonNull filterChain: FilterChain
    ) {
        val jwt = jwtUtils.parseJwt(request)

        if (jwt != null) {
            try {
                val claims = jwtUtils.validateJwtToken(jwt)
                val email = claims.body.subject
                val userDetails: UserDetails = userDetailsService.loadUserByUsername(email)
                val authentication = UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.authorities
                )
                authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                SecurityContextHolder.getContext().authentication = authentication
            } catch (e: Exception) {
                resolver.resolveException(request, response, null, e)
                return
            }
        }

        filterChain.doFilter(request, response)
    }
}