package com.modzo.security.resources.admin.users.update

import com.modzo.security.domain.users.User
import com.modzo.security.domain.users.commands.UpdateUserData
import org.hibernate.validator.constraints.Email
import org.hibernate.validator.constraints.NotBlank
import org.hibernate.validator.constraints.NotEmpty

import javax.validation.constraints.NotNull

class UpdateUserDataRequest {
    @NotBlank
    @Email
    String email

    @NotNull
    Boolean enabled

    @NotNull
    Boolean accountNotExpired

    @NotNull
    Boolean credentialsNonExpired

    @NotNull
    Boolean accountNotLocked

    @NotEmpty
    Set<User.Authority> authorities

    UpdateUserData toUserData(String uniqueId) {
        return new UpdateUserData(
                uniqueId:uniqueId,
                email: email,
                enabled: enabled,
                accountNotExpired: accountNotExpired,
                credentialsNonExpired: credentialsNonExpired,
                accountNotLocked: accountNotLocked,
                authorities: authorities
        )
    }
}
