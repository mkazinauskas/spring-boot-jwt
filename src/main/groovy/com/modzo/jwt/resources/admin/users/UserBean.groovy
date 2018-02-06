package com.modzo.jwt.resources.admin.users

import com.modzo.jwt.domain.User

class UserBean {
    String uniqueId
    String email
    boolean enabled
    boolean accountNotExpired
    boolean credentialsNonExpired
    boolean accountNotLocked
    Set<String> authorities

    static UserBean from(User user) {
        return new UserBean(
                uniqueId: user.uniqueId,
                email: user.email,
                enabled: user.enabled,
                accountNotExpired: user.accountNotExpired,
                credentialsNonExpired: user.credentialsNonExpired,
                accountNotLocked: user.accountNotLocked,
                authorities: user.authorities.collect { it.name() }.toSet()
        )
    }
}
