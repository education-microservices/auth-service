package com.example.azure.persistence.repository;

import com.example.azure.persistence.models.ProductModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<ProductModel, String> {

    List<ProductModel> findByNameRegex(String name);

    List<ProductModel> findByDescriptionRegex(String description);

}
