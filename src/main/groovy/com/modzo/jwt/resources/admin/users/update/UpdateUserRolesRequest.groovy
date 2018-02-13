package com.modzo.jwt.resources.admin.users.update

import com.modzo.jwt.domain.users.User
import org.hibernate.validator.constraints.NotEmpty

class UpdateUserRolesRequest {
    @NotEmpty
    Set<User.Authority> roles
}
