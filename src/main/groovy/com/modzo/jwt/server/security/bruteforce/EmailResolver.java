package com.modzo.jwt.server.security.bruteforce;

import com.modzo.jwt.email.EmailException;
import com.modzo.jwt.server.security.LocalClientDetailsService;
import com.modzo.jwt.server.security.LocalUserDetailsService;
import org.springframework.security.core.Authentication;

public class EmailResolver {

    public static String resolve(Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof LocalUserDetailsService.LocalUserDetails) {
            return ((LocalUserDetailsService.LocalUserDetails) principal).getUsername();
        }
        if (principal instanceof LocalClientDetailsService.LocalClientDetails) {
            return ((LocalClientDetailsService.LocalClientDetails) principal).getClientId();
        }
        if (principal instanceof String) {
            return (String) principal;
        }
        throw new EmailException("Unable to resolve authentication email");
    }
}
