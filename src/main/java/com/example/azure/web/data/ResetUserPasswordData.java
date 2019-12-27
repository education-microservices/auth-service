package com.example.azure.web.data;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ResetUserPasswordData {

    @NotBlank
    @Size(min = 6)
    private String password;
}
