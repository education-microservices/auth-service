package com.example.azure.persistence.repository;

import com.example.azure.persistence.models.UserActivationRequestModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserActivationRequestRepository extends MongoRepository<UserActivationRequestModel, String> {
}
