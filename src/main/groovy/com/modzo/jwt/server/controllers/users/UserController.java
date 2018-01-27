package com.modzo.jwt.server.controllers.users;

import com.modzo.jwt.server.controllers.GeneralController;
import com.modzo.jwt.server.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.security.Principal;

@RestController
public class UserController {

    @Autowired
    UserActionService accountService;

    @PreAuthorize("hasRole('REGISTER')")
    @PostMapping("/api/account/register")
    public ResponseEntity registerAccount(@RequestBody RegisterUserRequest account) {
        User user = accountService.registerUser(account);
        return ResponseEntity.created(URI.create("")).build();
    }

    @PreAuthorize("isFullyAuthenticated()")
    @DeleteMapping("/api/account/remove")
    public ResponseEntity<GeneralController.RestMsg> removeAccount(Principal principal) {
        boolean isDeleted = accountService.removeAuthenticatedAccount();
        if (isDeleted) {
            return new ResponseEntity<>(
                    new GeneralController.RestMsg(String.format("[%s] removed.", principal.getName())),
                    HttpStatus.OK
            );
        } else {
            return new ResponseEntity<GeneralController.RestMsg>(
                    new GeneralController.RestMsg(String.format("An error ocurred while delete [%s]", principal.getName())),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
