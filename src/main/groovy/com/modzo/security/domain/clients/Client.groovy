package com.modzo.security.domain.clients

import groovy.transform.CompileStatic
import org.hibernate.validator.constraints.NotBlank

import javax.persistence.*

import static javax.persistence.GenerationType.SEQUENCE
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric

@CompileStatic
@Entity
@Table(name = 'clients')
class Client {
    @Id
    @GeneratedValue(generator = 'clients_sequence', strategy = SEQUENCE)
    @SequenceGenerator(name = 'clients_sequence', sequenceName = 'clients_sequence', allocationSize = 1)
    @Column(name = 'id')
    Long id

    @NotBlank
    @Column(name = 'unique_id', unique = true, length = 10)
    String uniqueId = randomAlphanumeric(10)

    @Version
    @Column(name = 'version', nullable = false)
    long version

    @NotBlank
    @Column(name = 'client_id', unique = true)
    String clientId

    @Column(name = 'client_encoded_secret')
    String clientEncodedSecret

    @Column(name = 'secret_required')
    boolean secretRequired

    @Column(name = 'scoped')
    boolean scoped

    @Column(name = 'enabled')
    boolean enabled

    @Column(name = 'auto_approve')
    boolean autoApprove

    @Column(name = 'access_token_validity_seconds')
    int accessTokenValiditySeconds = 3600

    @Column(name = 'refresh_token_validity_seconds')
    int refreshTokenValiditySeconds = 36000

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = 'client_authorities', joinColumns = @JoinColumn(name = 'client_id', nullable = false))
    @Column(name = 'authority')
    final Set<Authority> authorities = []

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = 'client_scopes', joinColumns = @JoinColumn(name = 'client_id', nullable = false))
    @Column(name = 'scope')
    final Set<Scope> scopes = []

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = 'client_authorized_grant_types',
            joinColumns = @JoinColumn(name = 'client_id', nullable = false))

    @Column(name = 'authorized_grant_type')
    final Set<GrantType> authorizedGrantTypes = []

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = 'client_registered_redirect_uris',
            joinColumns = @JoinColumn(name = 'client_id', nullable = false)
    )
    @Column(name = 'uri')
    final Set<String> registeredRedirectUris = []

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = 'client_resource_ids',
            joinColumns = @JoinColumn(name = 'client_id', nullable = false)
    )
    @Column(name = 'resource_id')
    final Set<String> resourceIds = []

    static enum Authority {
        ADMIN, CLIENT
    }

    static enum GrantType {
        IMPLICIT('implicit'),
        PASSWORD('password'),
        REFRESH_TOKEN('refresh_token'),
        AUTHORIZATION_CODE('authorization_code')
        final String type

        GrantType(String type) {
            this.type = type
        }
    }

    static enum Scope {
        READ('read'),
        WRITE('write')

        final String type

        Scope(String type) {
            this.type = type
        }
    }
}

