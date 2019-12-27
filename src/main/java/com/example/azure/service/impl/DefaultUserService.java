package com.example.azure.service.impl;

import com.example.azure.persistence.models.*;
import com.example.azure.persistence.repository.LoginRepository;
import com.example.azure.persistence.repository.UserActivationRequestRepository;
import com.example.azure.persistence.repository.UserPasswordChangeRequestRepository;
import com.example.azure.persistence.repository.UserRepository;
import com.example.azure.service.EmailService;
import com.example.azure.service.UserService;
import com.example.azure.utils.ErrorUtil;
import com.example.azure.utils.RandomUtil;
import com.example.azure.web.data.LoginData;
import com.example.azure.web.data.RegisterUserData;
import com.example.azure.web.validator.UserDataValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.function.Supplier;

@Service
public class DefaultUserService implements UserService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultUserService.class);

    private static final Supplier<IllegalStateException> USER_DOES_NOT_EXISTS = () -> new IllegalStateException("User does not exists");

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmailService emailService;
    @Autowired
    private RandomUtil randomUtil;
    @Autowired
    private UserActivationRequestRepository userActivationRequestRepository;
    @Autowired
    private UserPasswordChangeRequestRepository userPasswordChangeRequestRepository;
    @Autowired
    private UserDataValidator userDataValidator;
    @Autowired
    private ErrorUtil errorUtil;
    @Autowired
    private LoginRepository loginRepository;

    @Override
    public UserModel getUserForId(String id) {
        return userRepository.findById(id).orElseThrow(() -> errorUtil.getUserNotFoundError(id));
    }

    @Override
    public UserModel getUserForEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> errorUtil.getUserNotFoundError(email));
    }

    @Override
    public UserModel save(UserModel user) {
        userRepository.save(user);
        return user;
    }

    @Override
    @Transactional
    public UserModel registerUser(final RegisterUserData userData) {
        final UserModel userModel = createNewUser(userData);
        final UserActivationRequestModel userActivationRequestModel = createActivationRequest(userModel);
        emailService.sendUserActivationEmail(userModel, userActivationRequestModel);
        return userModel;
    }

    @Override
    public void activateUser(String activationCode) {
        final UserActivationRequestModel requestModel = userActivationRequestRepository
                .findById(activationCode)
                .orElseThrow(IllegalStateException::new);
        if (RequestStatusEnum.OPEN == requestModel.getRequestStatus()) {
            doUserActivation(requestModel);
        }
    }

    @Override
    public LoginData login(String email, String password) {
        final UserModel userModel = getUserForEmail(email);
        if (UserStatusEnum.ACTIVE != userModel.getUserStatus()) {
            errorUtil.propagateUserIsNotActiveError();
        }
        if (!passwordEncoder.matches(password, userModel.getPassword())) {
            errorUtil.propagateInvalidCredentialsError();
        }
        final LoginModel loginModel = loginRepository
                .findByUserIdOrderByCreatedDesc(userModel.getId())
                .stream().findFirst().orElseGet(() -> createNewLoginModel(userModel));
        return LoginData.builder()
                .accessToken(loginModel.getAccessToken())
                .userId(loginModel.getUserId()).build();
    }

    @Override
    public void passwordResetInitiate(String email) {
        final UserModel userModel = getUserForEmail(email);
        final UserPasswordChangeRequestModel passwordResetRequest = UserPasswordChangeRequestModel.builder()
                .id(randomUtil.newRandomAlphaNumericString(128))
                .created(new Date())
                .usedId(userModel.getId())
                .build();
        userPasswordChangeRequestRepository.save(passwordResetRequest);
        emailService.sendUserPasswordResetEmail(userModel, passwordResetRequest);
    }

    @Override
    public void passwordResetSubmit(String resetCode, String newPassword) {
        final UserPasswordChangeRequestModel passwordResetRequest = userPasswordChangeRequestRepository
                .findById(resetCode)
                .orElseThrow(IllegalStateException::new);
        final UserModel userModel = getUserForId(passwordResetRequest.getUsedId());
        final String newPasswordEncoded = passwordEncoder.encode(newPassword);
        userModel.setPassword(newPasswordEncoded);
        userRepository.save(userModel);
        passwordResetRequest.setRequestStatus(RequestStatusEnum.CLOSED);
        userPasswordChangeRequestRepository.save(passwordResetRequest);
    }

    private LoginModel createNewLoginModel(UserModel userModel) {
        final LoginModel loginModel = LoginModel.builder()
                .accessToken(randomUtil.newRandomAlphaNumericString(256))
                .userId(userModel.getId())
                .created(new Date())
                .build();
        loginRepository.save(loginModel);
        return loginModel;
    }

    private void doUserActivation(UserActivationRequestModel requestModel) {
        final UserModel userModel = getUserForId(requestModel.getUserId());
        userModel.setUserStatus(UserStatusEnum.ACTIVE);
        userRepository.save(userModel);
        requestModel.setUpdated(new Date());
        requestModel.setRequestStatus(RequestStatusEnum.CLOSED);
        userActivationRequestRepository.save(requestModel);
    }

    private UserModel createNewUser(RegisterUserData userData) {
        final UserModel userModel = convertToUserModel(userData);
        userRepository.save(userModel);
        return userModel;
    }

    private UserActivationRequestModel createActivationRequest(final UserModel userModel) {
        final UserActivationRequestModel userActivationRequestModel = UserActivationRequestModel.builder()
                .id(randomUtil.newRandomAlphaNumericString(64))
                .userId(userModel.getId())
                .created(new Date()).build();
        userActivationRequestRepository.save(userActivationRequestModel);
        return userActivationRequestModel;
    }

    private UserModel convertToUserModel(RegisterUserData userData) {

        final String password = passwordEncoder.encode(userData.getPassword());
        LOG.info("Password: {}", password);

        return UserModel.builder()
                .email(userData.getEmail())
                .firstName(userData.getFirstName())
                .lastName(userData.getLastName())
                .password(password).build();
    }
}
