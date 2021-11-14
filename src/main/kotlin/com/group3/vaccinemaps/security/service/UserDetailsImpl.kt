package com.group3.vaccinemaps.security.service

import com.group3.vaccinemaps.entity.User
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserDetailsImpl(val user: User) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return user.roles.map { role -> SimpleGrantedAuthority(role.name.name) }
    }

    override fun getPassword() = user.password

    override fun getUsername() = user.email

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true
}