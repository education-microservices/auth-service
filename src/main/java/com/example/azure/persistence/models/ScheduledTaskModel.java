package com.example.azure.persistence.models;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document(collection = "scheduledtasks")
public class ScheduledTaskModel extends ItemModel {

    private Date latestExecutionTime;
    private long recurrenceMillis;
    private String scheduledTaskClass;
    private boolean active;
}
