package com.modzo.jwt.resources.admin.users.update

import com.modzo.jwt.domain.Role
import org.hibernate.validator.constraints.NotEmpty

class UpdateUserRolesRequest {
    @NotEmpty
    Set<Role> roles
}
