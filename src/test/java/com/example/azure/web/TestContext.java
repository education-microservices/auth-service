package com.example.azure.web;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class TestContext {

    private final Map<String, Object> testAttributes = new LinkedHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(final String key) {
        Object value = testAttributes.get(key);
        if (value != null) {
            return (T) value;
        } else {
            throw new NullPointerException("Not attribute for key ["+ key +"]");
        }
    }

    public TestContext addAttribute(final String key, final Object value) {
        testAttributes.put(key, value);
        return this;
    }
}
