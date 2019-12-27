package com.example.azure.web.cilent;

import com.example.azure.persistence.models.UserModel;
import com.example.azure.web.data.LoginData;
import com.example.azure.web.data.RegisterUserData;
import com.example.azure.web.data.ResetUserPasswordData;
import com.example.azure.web.responses.Responses;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface UserServiceClient {

    @GetMapping("/v1/users")
    List<UserModel> getUsers();

    @PostMapping("/v1/users/register")
    ResponseEntity<?> usersRegister(final RegisterUserData userData);

    @GetMapping("/v1/users/activate/{activationCode}")
    ResponseEntity<?> usersActivate(final @PathVariable String activationCode);

    @GetMapping("/v1/users/login")
    ResponseEntity<LoginData> usersLogin(final @RequestParam String email, final @RequestParam String password);

    @GetMapping("/v1/users/passwords/change/initiate")
    ResponseEntity<?> usersPasswordResetInitiate(final @RequestParam String email);

    @PutMapping("/v1/users/passwords/change/submit")
    ResponseEntity<?> usersPasswordResetSubmit(final @RequestParam String resetCode, final @Valid @RequestBody ResetUserPasswordData resetUserPasswordData);
}
