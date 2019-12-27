package com.example.azure.persistence.repository;

import com.example.azure.persistence.models.ScheduledTaskModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledTaskRepository extends MongoRepository<ScheduledTaskModel, String> {
}
