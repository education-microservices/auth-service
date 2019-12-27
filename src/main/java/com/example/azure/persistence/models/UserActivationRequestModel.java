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
@Document(collection = "useractivationrequests")
public class UserActivationRequestModel {

    @Id
    private String id;
    private String userId;
    private String activationCode;
    private Date created;
    private Date updated;
    @Builder.Default
    private RequestStatusEnum requestStatus = RequestStatusEnum.OPEN;
}
