package com.example.azure.web.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class Responses {

    public static final String MSG = "msg";

    public static ResponseEntity ok (String msg) {
        final Map<String, String> responseBody = new HashMap<>();
        responseBody.put(MSG, msg);
        return ResponseEntity.ok().body(responseBody);
    }

    public static ResponseEntity created (String msg) {
        final Map<String, String> responseBody = new HashMap<>();
        responseBody.put(MSG, msg);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseBody);
    }

    public static ResponseEntity notFound (String msg) {
        final Map<String, String> responseBody = new HashMap<>();
        responseBody.put(MSG, msg);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseBody);
    }
}
