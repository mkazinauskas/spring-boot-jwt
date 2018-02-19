package com.modzo.jwt.server.security;

import com.modzo.jwt.domain.users.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import static com.modzo.jwt.domain.users.User.Authority.REGISTERED_USER;

@EnableResourceServer
@Configuration
class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/api/admin", "/api/user").authenticated()
                .antMatchers("/api/management/**").hasAuthority(REGISTERED_USER.name())
                .antMatchers("/tokens").permitAll();
    }
}
