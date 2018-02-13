package com.modzo.jwt.domain.clients

import groovy.transform.CompileStatic
import org.springframework.data.jpa.repository.JpaRepository

@CompileStatic
interface Clients extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name)

    Optional<Client> findByUniqueId(String uniqueId)
}
