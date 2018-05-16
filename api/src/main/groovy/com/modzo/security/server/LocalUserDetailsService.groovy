package com.modzo.security.server

import com.modzo.domain.users.User
import com.modzo.domain.users.Users
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

import static com.modzo.domain.DomainException.userByEmailWasNotFound

@Component
class LocalUserDetailsService implements UserDetailsService {

    private final Users users

    LocalUserDetailsService(Users users) {
        this.users = users
    }

    @Override
    @Transactional
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = users.findByEmail(email).orElseThrow { userByEmailWasNotFound(email) }

        return new LocalUserDetails(user)
    }

    static class LocalUserDetails implements UserDetails {

        String username

        String password

        boolean accountNonExpired

        boolean accountNonLocked

        boolean credentialsNonExpired

        boolean enabled

        Set<LocalGrantedAuthority> authorities = []

        static class LocalGrantedAuthority implements GrantedAuthority {
            String authority

            LocalGrantedAuthority(String authority) {
                this.authority = authority
            }
        }

        LocalUserDetails(User user) {
            username = user.email
            password = user.encodedPassword
            accountNonExpired = user.accountNotExpired
            accountNonLocked = user.accountNotLocked
            credentialsNonExpired = user.credentialsNonExpired
            enabled = user.enabled
            authorities.addAll(user.authorities.collect { new LocalGrantedAuthority(it.name()) })
        }
    }
}
