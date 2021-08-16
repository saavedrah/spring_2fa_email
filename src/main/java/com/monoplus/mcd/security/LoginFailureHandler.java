package com.monoplus.mcd.security;

import com.monoplus.mcd.model.LoginUser;
import com.monoplus.mcd.services.LoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginFailureHandler.class);

    @Autowired
    private LoginUserService loginUserService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception)
            throws IOException, ServletException {

        String email = request.getParameter("email");
        request.setAttribute("email", email);

        LOGGER.info("************ Local login Failure. email: {} - exception: {}", email,
                (exception == null ? "null exception" : exception.getMessage()));

        String redirectURL = "/login?error&email=" + email;

        if (exception.getMessage().contains("OTP")) {
            redirectURL = "/login?otp=true&email=" + email;
        } else {
            LoginUser loginUser = loginUserService.getLoginUserByEmail(email);
            if (loginUser.isOTPRequired()) {
                redirectURL = "/login?otp=true&email=" + email;
            }
        }

        LOGGER.info("Redirect destination: {}", redirectURL);
        super.setDefaultFailureUrl(redirectURL);
        super.setUseForward(true);
        super.onAuthenticationFailure(request, response, exception);
    }

}
