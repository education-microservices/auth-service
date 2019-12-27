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
@Document(collection = "userpasswordrequests")
public class UserPasswordChangeRequestModel {

    @Id
    private String id;
    private String usedId;
    private Date created;
    private Date update;
    @Builder.Default
    private RequestStatusEnum requestStatus = RequestStatusEnum.OPEN;
}
