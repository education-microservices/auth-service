package com.example.azure.web.validator;

import com.example.azure.persistence.repository.UserRepository;
import com.example.azure.utils.ErrorUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserDataValidator {

    @Autowired
    private ErrorUtil errorUtil;
    @Autowired
    private UserRepository userRepository;

    public void checkUniqueEmail(final String email) {
        if (userRepository.existsByEmail(email)) {
            errorUtil.propagateUserAlreadyExistsError(email);
        }
    }

    public void checkUserExists(String userId) {
        if (!userRepository.existsById(userId)) {
            errorUtil.propagateUserNotFoundError(userId);
        }
    }
}
