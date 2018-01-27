package com.modzo.jwt.server.domain

import groovy.transform.CompileStatic
import org.springframework.data.jpa.repository.JpaRepository

@CompileStatic
interface Users extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email)

    User save(User account)

    int deleteAccountById(Long id)
}
