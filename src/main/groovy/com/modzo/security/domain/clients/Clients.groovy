package com.modzo.security.domain.clients

import org.springframework.data.jpa.repository.JpaRepository

interface Clients extends JpaRepository<Client, Long> {
    Optional<Client> findByClientId(String clientId)

    Optional<Client> findByUniqueId(String uniqueId)
}
