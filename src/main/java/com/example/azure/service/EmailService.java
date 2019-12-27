package com.example.azure.service;

import com.example.azure.persistence.models.UserActivationRequestModel;
import com.example.azure.persistence.models.UserModel;
import com.example.azure.persistence.models.UserPasswordChangeRequestModel;

public interface EmailService {

    void sendUserActivationEmail(UserModel userModel, UserActivationRequestModel userActivationRequestModel);

    void sendUserPasswordResetEmail(UserModel userModel, UserPasswordChangeRequestModel userPasswordChangeRequestModel);
}
