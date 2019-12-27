package com.example.azure.persistence.repository;

import com.example.azure.persistence.models.UserActivationRequestModel;
import com.example.azure.persistence.models.UserPasswordChangeRequestModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordChangeRequestRepository extends MongoRepository<UserPasswordChangeRequestModel, String> {
}
