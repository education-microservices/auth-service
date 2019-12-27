package com.example.azure.web.data;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LoginData {

    private String accessToken;
    private String userId;
}
