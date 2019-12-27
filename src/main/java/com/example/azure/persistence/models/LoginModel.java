package com.example.azure.persistence.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document("logins")
public class LoginModel {

    @Id
    private String accessToken;
    private String userId;
    private Date created;
    @Builder.Default
    private long expirationMillis = 120000;
}
