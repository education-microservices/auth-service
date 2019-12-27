package com.example.azure.web.exceptions;

import com.example.azure.web.responses.Responses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class IllegalStateHandler {

    @ExceptionHandler({ HttpClientErrorException.class })
    public final ResponseEntity<?> handleException(HttpClientErrorException ex, WebRequest request) {
        final Map<String, String> msg = new HashMap<>();
        msg.put("msg", ex.getStatusText());
        return ResponseEntity.status(ex.getStatusCode()).body(msg);
    }
}
