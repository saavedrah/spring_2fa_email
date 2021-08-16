package com.monoplus.mcd.security;

import com.monoplus.mcd.model.LoginUser;
import com.monoplus.mcd.services.LoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginSuccessHandler.class);

    @Autowired
    private LoginUserService loginUserService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {

        LoginUserDetails loginUserDetails
                = (LoginUserDetails) authentication.getPrincipal();

        LoginUser loginUser = loginUserDetails.getLoginUser();
        LOGGER.info("************ Local login successful. LoginUser: {}", loginUser);

        if (loginUser.isOTPRequired()) {
            loginUserService.clearOTP(loginUser);
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }

}
