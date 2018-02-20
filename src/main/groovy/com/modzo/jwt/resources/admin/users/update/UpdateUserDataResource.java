package com.modzo.jwt.resources.admin.users.update;

import com.modzo.jwt.domain.users.commands.UpdateUserData;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class UpdateUserDataResource {

    private final UpdateUserData.Handler handler;

    public UpdateUserDataResource(UpdateUserData.Handler handler) {
        this.handler = handler;
    }

    @PutMapping("/api/admin/users/{uniqueId}")
    ResponseEntity updateRoles(@PathVariable("uniqueId") String uniqueId,
                               @RequestBody UpdateUserDataRequest data) {
        handler.handle(data.toUserData(uniqueId));
        return ResponseEntity.noContent().build();
    }
}
