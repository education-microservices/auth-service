package com.example.azure.persistence.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document(collection = "users")
public class UserModel extends ItemModel {

    private String firstName;
    private String lastName;
    private String email;
    @JsonIgnore
    private String password;

    @Builder.Default
    private UserStatusEnum userStatus = UserStatusEnum.CREATED;

    @Builder.Default
    private List<AddressModel> addresses = new ArrayList<>();
}
