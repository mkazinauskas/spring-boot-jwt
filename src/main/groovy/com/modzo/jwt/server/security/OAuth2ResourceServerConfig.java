package com.modzo.jwt.server.security;

import com.modzo.jwt.domain.users.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import static com.modzo.jwt.domain.users.User.Authority.ADMIN;
import static com.modzo.jwt.domain.users.User.Authority.USER;

@EnableResourceServer
@Configuration
class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/admin/**").hasAuthority(ADMIN.name())
                .antMatchers("/api/user/**").hasAuthority(USER.name())
                .antMatchers("/api/management/**").hasAnyAuthority(User.Authority.stringValues());
    }
}
