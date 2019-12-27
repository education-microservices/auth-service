package com.example.azure.utils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.nio.charset.Charset;
import java.util.Random;

@Service
public class RandomUtil {

    public String newRandomAlphaNumericString(int length) {
        return RandomStringUtils.randomAlphanumeric(length);
    }
}
