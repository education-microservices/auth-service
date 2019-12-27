package com.example.azure.persistence.repository;

import com.example.azure.persistence.models.ItemModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends MongoRepository<ItemModel, String> { }