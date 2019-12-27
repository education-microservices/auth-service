package com.example.azure.service;

import com.example.azure.persistence.models.UserModel;
import com.example.azure.web.data.LoginData;
import com.example.azure.web.data.RegisterUserData;

public interface UserService {

    UserModel getUserForId(String id);

    UserModel getUserForEmail(String email);

    UserModel save(UserModel user);

    UserModel registerUser(RegisterUserData userData);

    void activateUser(String activationCode);

    LoginData login(String email, String password);

    void passwordResetInitiate(String email);

    void passwordResetSubmit(String resetCode, String newPassword);
}
