package com.modzo.jwt.resources.admin.clients;

import com.modzo.jwt.domain.DomainException;
import com.modzo.jwt.domain.clients.Clients;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.modzo.jwt.domain.DomainException.clientByUniqueIdWasNotFound;
import static org.springframework.http.HttpStatus.OK;

@RestController
class ClientsResource {

    private final Clients clients;

    public ClientsResource(Clients clients) {
        this.clients = clients;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/clients/{uniqueId}")
    @ResponseStatus(OK)
    ResponseEntity<ClientBean> getClient(@PathVariable("uniqueId") String uniqueId) {
        ClientBean user = clients.findByUniqueId(uniqueId).map(ClientBean::from)
                .orElseThrow(() -> clientByUniqueIdWasNotFound(uniqueId));
        return ResponseEntity.ok(user);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/clients")
    @ResponseStatus(OK)
    ResponseEntity<Page<ClientBean>> getClients(Pageable pageable) {
        Page<ClientBean> result = clients.findAll(pageable).map(ClientBean::from);
        return ResponseEntity.ok(result);
    }
}
