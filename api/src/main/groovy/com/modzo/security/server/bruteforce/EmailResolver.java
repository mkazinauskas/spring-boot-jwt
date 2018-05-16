package com.modzo.security.server.bruteforce;

import com.modzo.email.EmailException;
import com.modzo.security.server.LocalClientDetailsService;
import com.modzo.security.server.LocalUserDetailsService;
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
