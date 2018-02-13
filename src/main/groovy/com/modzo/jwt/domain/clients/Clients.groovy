package com.modzo.jwt.domain.clients

import org.springframework.data.jpa.repository.JpaRepository

interface Clients extends JpaRepository<Client, Long> {
    Optional<Client> findByName(String name)

    Optional<Client> findByUniqueId(String uniqueId)
}
