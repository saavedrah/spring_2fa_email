package com.monoplus.mcd.configuration;

import com.monoplus.mcd.security.BeforeAuthenticationFilter;
import com.monoplus.mcd.security.LoginFailureHandler;
import com.monoplus.mcd.security.LoginSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private BeforeAuthenticationFilter beforeLoginFilter;

    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

    @Autowired
    private LoginFailureHandler loginFailureHandler;

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Before authentication filter
        http.addFilterBefore(beforeLoginFilter, BeforeAuthenticationFilter.class);

        http.formLogin()
                .loginPage("/login")
                .usernameParameter("email")
                // Successful login
                .successHandler(loginSuccessHandler)
                // Login failure
                .failureHandler(loginFailureHandler)
                .permitAll();

        http.logout()
                .permitAll();

        http.authorizeRequests()
                .anyRequest().authenticated();

        http.csrf().disable();
    }
}
