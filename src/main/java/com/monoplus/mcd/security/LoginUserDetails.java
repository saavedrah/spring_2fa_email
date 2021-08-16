package com.monoplus.mcd.security;

import com.monoplus.mcd.model.LoginUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class LoginUserDetails implements UserDetails {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserDetails.class);

    private LoginUser loginUser;

    public LoginUserDetails (LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        if (loginUser.isOTPRequired()) {
            LOGGER.info("************ OTPRequired - Password is OTP: {}", loginUser.getOneTimePassword());
            return loginUser.getOneTimePassword();
        }

        LOGGER.info("************ OTP NOT Required - Password: {}", loginUser.getPassword());
        return loginUser.getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public LoginUser getLoginUser() {
        return this.loginUser;
    }
}
