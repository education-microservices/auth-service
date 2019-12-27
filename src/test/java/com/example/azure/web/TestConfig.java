package com.example.azure.web;

import com.example.azure.persistence.models.UserActivationRequestModel;
import com.example.azure.persistence.models.UserModel;
import com.example.azure.persistence.models.UserPasswordChangeRequestModel;
import com.example.azure.service.EmailService;
import feign.Logger;
import feign.Request;
import feign.Response;
import feign.Util;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

import static feign.Util.UTF_8;
import static feign.Util.decodeOrDefault;

@Configuration
public class TestConfig {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(TestConfig.class);

    public static final String EMAIL_SERVICE_ACTIVATE_REGISTER_USER_MODEL = "emailService.sendUserActivationEmail.registerUserModel";
    public static final String EMAIL_SERVICE_ACTIVATE_USER_ACTIVATION_REQUEST_MODEL = "emailService.sendUserActivationEmail.userActivationRequestModel";
    public static final String EMAIL_SERVICE_RESET_USER_MODEL = "emailService.sendUserPasswordResetEmail.registerUserModel";
    public static final String EMAIL_SERVICE_RESET_ACTIVATION_REQUEST_MODEL = "emailService.sendUserPasswordResetEmail.userActivationRequestModel";


    @Bean
    public feign.Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public EmailService emailService() {
        return new EmailService() {

            @Autowired
            private TestContext testContext;

            @Override
            public void sendUserActivationEmail(UserModel userModel, UserActivationRequestModel userActivationRequestModel) {
                testContext.addAttribute(EMAIL_SERVICE_ACTIVATE_REGISTER_USER_MODEL, userModel)
                        .addAttribute(EMAIL_SERVICE_ACTIVATE_USER_ACTIVATION_REQUEST_MODEL, userActivationRequestModel);
            }

            @Override
            public void sendUserPasswordResetEmail(UserModel userModel, UserPasswordChangeRequestModel userPasswordChangeRequestModel) {
                testContext.addAttribute(EMAIL_SERVICE_RESET_USER_MODEL, userModel)
                        .addAttribute(EMAIL_SERVICE_RESET_ACTIVATION_REQUEST_MODEL, userPasswordChangeRequestModel);
            }
        };
    }

    @Bean
    public feign.Logger logger() {
        final AtomicLong rqNumber = new AtomicLong();
        final ThreadLocal<Long> logCtx = new ThreadLocal<>();
        return new Logger() {
            @Override
            protected void log(String configKey, String format, Object... args) {

            }

            @Override
            protected void logRequest(String configKey, Level logLevel, Request request) {
                long currentRequestNumber = rqNumber.incrementAndGet();
                logCtx.set(currentRequestNumber);
                LOG.info("===================================================");
                LOG.info("------ Request #{} -------", currentRequestNumber);
                LOG.info("       " + "Method : " + request.httpMethod());
                LOG.info("       " + "Url    : " + request.url());
                LOG.info("       " + "Headers: " + request.headers());
                if (!request.httpMethod().equals(Request.HttpMethod.GET)) {
                    LOG.info("       " + "Body   : " + request.requestBody().asString());
                }
            }

            @Override
            protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
                byte[] bodyData = Util.toByteArray(response.body().asInputStream());
                LOG.info("===================================================");
                LOG.info("------ Response #{} -------", logCtx.get());
                LOG.info("       " + "Status : " + response.status());
                LOG.info("       " + "Headers: " + response.headers());
                LOG.info("       " + "Body   : " + decodeOrDefault(bodyData, UTF_8, ""));
                return response.toBuilder().body(bodyData).build();
            }
        };
    }
}
