package com.modzo.jwt.resources.admin.users;

import com.modzo.jwt.domain.Users;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.modzo.jwt.domain.exceptions.UserNotFoundException.byUniqueId;
import static org.springframework.http.HttpStatus.OK;

@RestController
class UsersResource {

    private final Users users;

    public UsersResource(Users users) {
        this.users = users;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/api/admin/users/{uniqueId}")
    @ResponseStatus(OK)
    ResponseEntity remove(@PathVariable("uniqueId") String uniqueId) {
        UserBean user = users.findByUniqueId(uniqueId).map(UserBean::from)
                .orElseThrow(() -> byUniqueId(uniqueId));
        return ResponseEntity.ok(user);
    }
}
