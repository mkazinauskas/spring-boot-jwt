package com.modzo.security.server;

import com.modzo.domain.users.User;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

import static com.modzo.domain.users.User.Authority.ADMIN;
import static com.modzo.domain.users.User.Authority.USER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@EnableResourceServer
@Configuration
class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(POST, "/api/user/password-reminder").permitAll()
                .antMatchers(GET, "/api/user/activation").permitAll()
                .antMatchers(POST, "/api/users").permitAll()
                .antMatchers("/api/admin/**").hasAuthority(ADMIN.name())
                .antMatchers("/api/user/**").hasAuthority(USER.name())
                .antMatchers("/api/management/**").hasAnyAuthority(User.Authority.stringValues());
    }
}
