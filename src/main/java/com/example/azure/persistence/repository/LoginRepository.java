package com.example.azure.persistence.repository;

import com.example.azure.persistence.models.LoginModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoginRepository extends MongoRepository<LoginModel, String> {

    List<LoginModel> findByUserIdOrderByCreatedDesc(String userId);
}
