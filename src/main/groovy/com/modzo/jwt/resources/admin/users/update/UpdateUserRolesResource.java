package com.modzo.jwt.resources.admin.users.update;

import com.modzo.jwt.domain.commands.UpdateUserRoles;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UpdateUserRolesResource {

    private final UpdateUserRoles.Handler handler;

    public UpdateUserRolesResource(UpdateUserRoles.Handler handler) {
        this.handler = handler;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/api/admin/users/{uniqueId}/roles")
    ResponseEntity updateRoles(@PathVariable("uniqueId") String uniqueId, @RequestBody UpdateUserRolesRequest data) {
        handler.handle(new UpdateUserRoles(uniqueId, data.getRoles()));
        return ResponseEntity.noContent().build();
    }
}
