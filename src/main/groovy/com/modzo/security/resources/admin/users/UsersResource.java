package com.modzo.security.resources.admin.users;

import com.modzo.security.domain.users.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static com.modzo.commons.domain.DomainException.userByUniqueIdWasNotFound;
import static org.springframework.http.HttpStatus.OK;

@RestController
class UsersResource {

    private final Users users;

    public UsersResource(Users users) {
        this.users = users;
    }

    @GetMapping("/api/admin/users/{uniqueId}")
    @ResponseStatus(OK)
    ResponseEntity<UserBean> getUser(@PathVariable("uniqueId") String uniqueId) {
        UserBean user = users.findByUniqueId(uniqueId).map(UserBean::from)
                .orElseThrow(() -> userByUniqueIdWasNotFound(uniqueId));
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/admin/users")
    @ResponseStatus(OK)
    ResponseEntity<Page<UserBean>> getUsers(Pageable pageable) {
        Page<UserBean> result = users.findAll(pageable).map(UserBean::from);
        return ResponseEntity.ok(result);
    }
}
