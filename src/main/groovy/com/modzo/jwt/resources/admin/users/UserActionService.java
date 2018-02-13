package com.modzo.jwt.resources.admin.users;

import com.modzo.jwt.domain.users.User;
import com.modzo.jwt.domain.users.Users;
import com.modzo.jwt.resources.admin.users.register.RegisterUserRequest;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class UserActionService implements UserDetailsService {

    private final Logger logger = Logger.getLogger(UserActionService.class);

    @Autowired
    private Users accountRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return accountRepo.findByEmail(email).map(user -> new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return user.getAuthorities().stream()
                        .map(role -> new SimpleGrantedAuthority(role.toString()))
                        .collect(Collectors.toList());
            }

            @Override
            public String getPassword() {
                return user.getEncodedPassword();
            }

            @Override
            public String getUsername() {
                return user.getEmail();
            }

            @Override
            public boolean isAccountNonExpired() {
                return user.getAccountNotExpired();
            }

            @Override
            public boolean isAccountNonLocked() {
                return user.isAccountNotLocked();
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return user.isCredentialsNonExpired();
            }

            @Override
            public boolean isEnabled() {
                return user.isEnabled();
            }
        }).orElseThrow(() -> new UsernameNotFoundException(String.format("Email[%s] not found", email)));
    }

    @Transactional(readOnly = true)
    User findAccountByUsername(String username) throws UsernameNotFoundException {
        Optional<User> account = accountRepo.findByEmail(username);
        if (account.isPresent()) {
            return account.get();
        } else {
            throw new UsernameNotFoundException(String.format("Username[%s] not found", username));
        }
    }

    public User registerUser(RegisterUserRequest account) {
        User user = new User();
        user.setEmail(account.getEmail());
        user.setEncodedPassword(passwordEncoder.encode(account.getPassword()));
        user.getAuthorities().add(User.Authority.ROLE_USER);
        return accountRepo.save(user);
    }

    @Transactional // To successfully remove the date @Transactional annotation must be added
    public boolean removeAuthenticatedAccount() throws UsernameNotFoundException {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User acct = findAccountByUsername(username);
        int del = accountRepo.deleteAccountById(acct.getId());
        return del > 0;
    }
}
