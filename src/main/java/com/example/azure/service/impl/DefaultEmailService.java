package com.example.azure.service.impl;

import com.example.azure.persistence.models.UserActivationRequestModel;
import com.example.azure.persistence.models.UserModel;
import com.example.azure.persistence.models.UserPasswordChangeRequestModel;
import com.example.azure.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class DefaultEmailService implements EmailService {

    private static final String ACTIVATION_SUBJECT = "Test API. Registration Confirmation";
    private static final String CHANGE_PASSWORD_SUBJECT = "Test API. Password Reset";

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public void sendUserActivationEmail(UserModel userModel, UserActivationRequestModel u) {

        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userModel.getEmail());
        mailMessage.setSubject(ACTIVATION_SUBJECT);
        mailMessage.setText("Please follow registration link <br> http://localhost:8080/v1/users/activate/" + u.getId());
        emailSender.send(mailMessage);
    }

    @Override
    public void sendUserPasswordResetEmail(UserModel userModel, UserPasswordChangeRequestModel m) {
        final SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(userModel.getEmail());
        mailMessage.setSubject(CHANGE_PASSWORD_SUBJECT);
        mailMessage.setText("Password reset link <br> http://localhost:8080/v1/users/passwords/change/submit?resetCode=" + m.getId());
        emailSender.send(mailMessage);
    }
}
