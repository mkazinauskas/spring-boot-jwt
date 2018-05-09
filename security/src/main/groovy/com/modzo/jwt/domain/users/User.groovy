package com.modzo.jwt.domain.users

import groovy.transform.CompileStatic
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank

import javax.persistence.*
import javax.validation.constraints.NotNull

import static javax.persistence.GenerationType.SEQUENCE
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@CompileStatic
@Entity
@Table(name = 'users')
class User {
    static final int DEFAULT_ACTIVATION_CODE_LENGTH = 10
    static final int DEFAULT_PASSWORD_RESET_CODE_LENGTH = 10

    @Id
    @GeneratedValue(generator = 'users_sequence', strategy = SEQUENCE)
    @SequenceGenerator(name = 'users_sequence', sequenceName = 'users_sequence', allocationSize = 1)
    @Column(name = 'id')
    Long id

    @NotBlank
    @Column(name = 'unique_id', unique = true, length = 10)
    String uniqueId = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(10)

    @Version
    @Column(name = 'version', nullable = false)
    long version

    @Email
    @NotNull
    @Column(name = 'email', unique = true)
    String email

    @NotNull
    @Column(name = 'encoded_password')
    String encodedPassword

    @Column(name = 'enabled')
    boolean enabled

    @Column(name = 'account_not_expired')
    boolean accountNotExpired

    @Column(name = 'credentials_not_expired')
    boolean credentialsNonExpired

    @Column(name = 'account_not_locked')
    boolean accountNotLocked

    @Column(name = 'password_reset_code', length = 32, nullable = true)
    String passwordResetCode

    @Column(name = 'activation_code', length = 32, nullable = true)
    String activationCode

    @NotNull
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = 'user_authorities', joinColumns = @JoinColumn(name = 'user_id', nullable = false))
    @Column(name = 'authority')
    final Set<Authority> authorities = []

    void activate() {
        enabled = true
        activationCode = null
    }

    void deactivate() {
        enabled = false
        activationCode = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(DEFAULT_ACTIVATION_CODE_LENGTH)
    }

    void newPasswordResetCode() {
        this.passwordResetCode = org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_RESET_CODE_LENGTH)
    }

    static enum Authority {
        REGISTERED_USER,
        USER,
        ADMIN

        static String[] stringValues() {
            return values()*.name() as String[]
        }
    }
}

