package com.modzo.jwt.resources.admin.users.update;

import com.modzo.jwt.domain.users.commands.UpdateUserAuthorities;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UpdateUserRolesResource {

    private final UpdateUserAuthorities.Handler handler;

    public UpdateUserRolesResource(UpdateUserAuthorities.Handler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/users/{uniqueId}/roles")
    ResponseEntity updateRoles(@PathVariable("uniqueId") String uniqueId, @RequestBody UpdateUserRolesRequest data) {
        handler.handle(new UpdateUserAuthorities(uniqueId, data.getRoles()));
        return ResponseEntity.noContent().build();
    }
}
