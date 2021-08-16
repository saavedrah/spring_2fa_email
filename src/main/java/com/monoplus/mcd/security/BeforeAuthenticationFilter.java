package com.monoplus.mcd.security;

import com.monoplus.mcd.model.LoginUser;
import com.monoplus.mcd.services.LoginUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;

@Component
public class BeforeAuthenticationFilter
        extends UsernamePasswordAuthenticationFilter {
    private static final Logger LOGGER = LoggerFactory.getLogger(BeforeAuthenticationFilter.class);

    private boolean postOnly = true;

    @Autowired
    private LoginUserService loginUserService;

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authManager) {
        super.setAuthenticationManager(authManager);
    }

    @Autowired
    @Override
    public void setAuthenticationFailureHandler(
            AuthenticationFailureHandler failureHandler) {
        super.setAuthenticationFailureHandler(failureHandler);
    }

    @Autowired
    @Override
    public void setAuthenticationSuccessHandler(
            AuthenticationSuccessHandler successHandler) {
        super.setAuthenticationSuccessHandler(successHandler);
    }

    public BeforeAuthenticationFilter() {
        setUsernameParameter("email");
        super.setRequiresAuthenticationRequestMatcher(
                new AntPathRequestMatcher("/login", "POST"));
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        if (this.postOnly && !request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        String email = request.getParameter("email");

        LOGGER.info("******************* attemptAuthentication- email: {}", email);

        LoginUser loginUser = loginUserService.getLoginUserByEmail(email);

        if (loginUser != null) {
            if (loginUser.isOTPRequired()) {
                LOGGER.info("******************* attemptAuthentication-OTPRequired-loginUser: {}", loginUser);
                return super.attemptAuthentication(request, response);
            } else {
                LOGGER.info("******************* attemptAuthentication-OTP NOT Required-loginUser: {}", loginUser);
            }

            float spamScore = getGoogleRecaptchaScore();
            if (spamScore < 0.5) {
                try {
                    loginUserService.generateOneTimePassword(loginUser);

                    LOGGER.info("******************* attemptAuthentication-generateOneTimePassword");

                    throw new InsufficientAuthenticationException("OTP");
                } catch (MessagingException | UnsupportedEncodingException ex) {
                    throw new AuthenticationServiceException(
                            "Error while sending OTP email.", ex);
                } catch (Exception e) {
                    throw new AuthenticationServiceException(e.getMessage(), e);
                }

            }
        }

        return super.attemptAuthentication(request, response);
    }

    private float getGoogleRecaptchaScore() {
        // call Google RECAPTHA API…
        return 0.47f;
    }

}
