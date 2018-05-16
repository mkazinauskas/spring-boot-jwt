package com.modzo.domain.users

import groovy.transform.CompileStatic
import org.springframework.data.jpa.repository.JpaRepository

@CompileStatic
interface Users extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email)

    Optional<User> findByUniqueId(String uniqueId)

    Optional<User> findByActivationCode(String activationCode)

    int deleteAccountById(Long id)
}
