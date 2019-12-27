
package com.example.azure.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class ErrorUtil {

    public void propagateUserNotFoundError(final String userId) {
        throw getUserNotFoundError(userId);
    }

    public void propagateUserAlreadyExistsError(final String email) {
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "User with such email [" + email + "] already exist in system");
    }

    public void propagateInvalidCredentialsError() {
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
    }

    public void propagateUserIsNotActiveError() {
        throw new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "User is not active");
    }

    public HttpClientErrorException getUserNotFoundError(final String userId) {
        return new HttpClientErrorException(HttpStatus.NOT_FOUND, "Such user [" + userId + "] does not exist in system");
    }
}