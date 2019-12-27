package com.example.azure.web.config;

import com.example.azure.persistence.models.UserModel;
import com.example.azure.persistence.repository.UserRepository;
import com.example.azure.service.UserService;
import com.example.azure.web.validator.UserDataValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.Optional;

@Component
public class AuthUserInterceptor implements HandlerInterceptor {

    private static final String USER_REQUEST_ATTRIBUTE = "user";
    public static final String USER_ID_PATH_VARIABLE = "userId";

    @Autowired
    private UserDataValidator userDataValidator;
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        final Map<String,String> path = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        if (path.containsKey(USER_ID_PATH_VARIABLE)) {
            final String userId = path.get(USER_ID_PATH_VARIABLE);
            request.setAttribute(USER_REQUEST_ATTRIBUTE, userService.getUserForId(userId));
        }

        return true;
    }
}
