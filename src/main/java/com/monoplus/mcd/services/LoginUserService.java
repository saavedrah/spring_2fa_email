package com.monoplus.mcd.services;

import com.monoplus.mcd.exception.McdAuthenticationException;
import com.monoplus.mcd.model.LoginUser;
import com.monoplus.mcd.model.LoginUserRepository;
import com.monoplus.mcd.security.LoginSuccessHandler;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class LoginUserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginUserService.class);

    @Autowired
    LoginUserRepository loginUserRepository;

    @Autowired
    JavaMailSender mailSender;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public void generateOneTimePassword(LoginUser loginUser)
            throws UnsupportedEncodingException, MessagingException {
        String OTP = RandomString.make(8);
        String encodedOTP = passwordEncoder().encode(OTP);

        loginUser.setOneTimePassword(encodedOTP);
        loginUser.setOtpRequestedTime(new Date());

        loginUserRepository.save(loginUser);

        sendOTPEmail(loginUser, OTP);
    }

    public void sendOTPEmail(LoginUser loginUser, String OTP)
            throws UnsupportedEncodingException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("info@pepisandbox.com", "Test");
        helper.setTo(loginUser.getEmail());

        String subject = "Here's your One Time Password (OTP) - Expire in 5 minutes!";

        String content = "<p>Hello " + loginUser.getUserName() + "</p>"
                + "<p>For security reason, you're required to use the following "
                + "One Time Password to login:</p>"
                + "<p><b>" + OTP + "</b></p>"
                + "<br>"
                + "<p>Note: this OTP is set to expire in 5 minutes.</p>";

        helper.setSubject(subject);

        helper.setText(content, true);

        mailSender.send(message);

    }

    public void clearOTP(LoginUser loginUser) {
        LOGGER.info("Clear OTP");

        loginUser.setOneTimePassword(null);
        loginUser.setOtpRequestedTime(null);
        loginUserRepository.save(loginUser);
    }

    public LoginUser getLoginUserByEmail(String email) throws AuthenticationException {
        List<LoginUser> loginUserList = loginUserRepository.findByEmail(email);
        if (loginUserList.isEmpty()) {
            throw new McdAuthenticationException(String.format("User with email address %s was not found.", email));
        }

        return loginUserList.get(0);
    }
}
