package com.woke.challenge.backend.infra.security

import com.woke.challenge.backend.services.AuthService
import com.woke.challenge.backend.services.TokenService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class SecurityFilter: OncePerRequestFilter() {

    @Autowired
    lateinit var tokenService: TokenService

    @Autowired
    lateinit var authService: AuthService

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = recoverToken(request)
        if (token != null) {
            val subject = tokenService.validateToken(token)
            val user = authService.loadUserByUsername(subject)
            val auth = UsernamePasswordAuthenticationToken(user, null, user.authorities)
            SecurityContextHolder.getContext().authentication = auth
        }
        filterChain.doFilter(request, response)
    }

    private fun recoverToken(request: HttpServletRequest): String? {
        val authHeader = request.getHeader("Authorization") ?: return null
        return authHeader.replace("Bearer ", "")
    }
}