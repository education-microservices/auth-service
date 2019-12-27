package com.example.azure.web;

import com.example.azure.persistence.models.UserModel;
import com.example.azure.persistence.repository.UserRepository;
import com.example.azure.service.UserService;
import com.example.azure.web.data.LoginData;
import com.example.azure.web.data.RegisterUserData;
import com.example.azure.web.data.ResetUserPasswordData;
import com.example.azure.web.responses.Responses;
import com.example.azure.web.validator.UserDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/v1/users")
public class UsersController {

    public static final String USER_SUCCESSFULLY_REGISTERED = "User successfully registered";
    public static final String USER_SUCCESSFULLY_ACTIVATED = "User successfully activated";
    public static final String PASSWORD_SUCCESSFULLY_CHANGED = "Password successfully changed";
    public static final String PASSWORD_RESET_INITIATED = "Password reset link sent onto your email box. Please check your email";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserDataValidator userDataValidator;

    @GetMapping("/{userId}")
    public UserModel getUserForId(@RequestAttribute UserModel user) {

        return user;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserModel> searchUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/register")
    public ResponseEntity<?> usersRegister(final @Valid @RequestBody RegisterUserData userData) {
        userDataValidator.checkUniqueEmail(userData.getEmail());
        userService.registerUser(userData);
        return Responses.created(USER_SUCCESSFULLY_REGISTERED);
    }

    @GetMapping("/activate/{activationCode}")
    public ResponseEntity<?> usersActivate(final @PathVariable String activationCode) {
        userService.activateUser(activationCode);
        return Responses.ok(USER_SUCCESSFULLY_ACTIVATED);
    }

    @GetMapping("/login")
    public ResponseEntity<LoginData> usersLogin(final @RequestParam String email, final @RequestParam String password) {
        final LoginData loginData = userService.login(email, password);
        return ResponseEntity.ok(loginData);
    }

    @GetMapping("/passwords/change/initiate")
    public ResponseEntity<?> usersPasswordResetInitiate(final @RequestParam String email) {
        userService.passwordResetInitiate(email);
        return Responses.ok(PASSWORD_RESET_INITIATED);
    }

    @PutMapping("/passwords/change/submit")
    public ResponseEntity<?> usersPasswordResetSubmit(final @RequestParam String resetCode, final @Valid @RequestBody ResetUserPasswordData resetUserPasswordData) {
        userService.passwordResetSubmit(resetCode, resetUserPasswordData.getPassword());
        return Responses.ok(PASSWORD_SUCCESSFULLY_CHANGED);
    }
}
