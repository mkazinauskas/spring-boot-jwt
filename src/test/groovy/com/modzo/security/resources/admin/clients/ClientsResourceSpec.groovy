package com.modzo.security.resources.admin.clients

import com.modzo.AbstractSpec
import com.modzo.Urls
import com.modzo.security.domain.clients.Client
import com.modzo.security.domain.clients.Clients
import com.modzo.security.domain.users.User

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.ParameterizedTypeReference
import org.springframework.hateoas.PagedResources
import org.springframework.http.ResponseEntity
import spock.lang.Shared

import static com.modzo.helpers.HttpEntityBuilder.builder
import static org.springframework.http.HttpMethod.GET
import static org.springframework.http.HttpStatus.FORBIDDEN
import static org.springframework.http.HttpStatus.OK

class ClientsResourceSpec extends AbstractSpec {

    @Autowired
    Clients clients

    @Shared
    String adminToken

    @Shared
    String userToken

    void setup() {
        adminToken = authorizationHelper.adminAccessToken()
        userToken = authorizationHelper.userAccessToken()
    }

    void 'should retrieve created client'() {
        given:
            Client client = clientHelper.createRegisteredClient()
        when:
            ResponseEntity<ClientBean> response = restTemplate.exchange(Urls.adminClient(client.uniqueId),
                    GET,
                    builder()
                            .bearer(adminToken)
                            .build(),
                    ClientBean
            )
        then:
            response.statusCode == OK
            response.body
            response.body.uniqueId == client.uniqueId
            response.body.version == client.version
            response.body.clientId == client.clientId
            response.body.enabled == client.enabled

            response.body.secretRequired == client.secretRequired
            response.body.scoped == client.scoped

            response.body.autoApprove == client.autoApprove
            response.body.accessTokenValiditySeconds == client.accessTokenValiditySeconds
            response.body.refreshTokenValiditySeconds == client.refreshTokenValiditySeconds
            response.body.authorities == client.authorities
            response.body.scopes == client.scopes
            response.body.authorizedGrantTypes == client.authorizedGrantTypes
            response.body.registeredRedirectUris == client.registeredRedirectUris
            response.body.resourceIds == client.resourceIds
    }

    void 'created client should be visible for admin only'() {
        given:
            User user = userHelper.createRegisteredUser(false)
        when:
            ResponseEntity<String> response = restTemplate.exchange(Urls.adminClient(user.uniqueId),
                    GET,
                    builder()
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }

    void 'should list all clients'() {
        given:
            clientHelper.createRegisteredClient()
        when:
            ResponseEntity<PagedResources<ClientBean>> response = restTemplate.exchange(Urls.adminClients(),
                    GET,
                    builder()
                            .bearer(adminToken)
                            .build(),
                    new ParameterizedTypeReference<PagedResources<ClientBean>>() {}
            )
        then:
            response.statusCode == OK
            response.body.content.size() > 1
        and:
            ClientBean clientBean = response.body.content.first()
            Client savedClient = clients.findByUniqueId(clientBean.uniqueId).get()

            clientBean.version == savedClient.version
            clientBean.clientId == savedClient.clientId
            clientBean.enabled == savedClient.enabled
            clientBean.secretRequired == savedClient.secretRequired
            clientBean.scoped == savedClient.scoped
            clientBean.autoApprove == savedClient.autoApprove
            clientBean.accessTokenValiditySeconds == savedClient.accessTokenValiditySeconds
            clientBean.refreshTokenValiditySeconds == savedClient.refreshTokenValiditySeconds
            clientBean.authorities == savedClient.authorities
            clientBean.scopes == savedClient.scopes
            clientBean.authorizedGrantTypes == savedClient.authorizedGrantTypes
            clientBean.registeredRedirectUris == savedClient.registeredRedirectUris
            clientBean.resourceIds == savedClient.resourceIds
    }

    void 'clients list should be visible for admin only'() {
        when:
            ResponseEntity<String> response = restTemplate.exchange(Urls.adminClients(),
                    GET,
                    builder()
                            .bearer(userToken)
                            .build(),
                    String
            )
        then:
            response.statusCode == FORBIDDEN
            response.body.contains('access_denied')
    }
}
