package com.modzo.jwt.server.domain

import groovy.transform.CompileStatic

import javax.persistence.Column;
import javax.persistence.Entity
import javax.persistence.GeneratedValue;
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.Version;

@Entity
@CompileStatic
@Table(name = 'tokens')
class Token {

    @Id
    @GeneratedValue
    @Column(name = 'id')
    Long id

    @Version
    @Column(name = 'version', nullable = false)
    long version

    @Column(name = 'jti', nullable = false, unique = true)
    String jti

    @Column(name = 'user_id', nullable = false)
    long userId

    @Column(name = 'expires', nullable = false)
    long expires

    @Column(name = 'blacklisted')
    boolean blackListed

    Token() {
    }

    Token(Long userId, String jti, Long expires) {
        this.jti = jti
        this.userId = userId;
        this.expires = expires;
    }
}
