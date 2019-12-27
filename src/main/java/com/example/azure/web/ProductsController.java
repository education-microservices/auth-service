package com.example.azure.web;

import com.example.azure.persistence.models.ProductModel;
import com.example.azure.persistence.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/products")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductModel createProduct(@RequestBody ProductModel productModel) {

        return productRepository.save(productModel);
    }

    @GetMapping
    public List<ProductModel> searchProduct(@RequestParam String q, @RequestParam (defaultValue = "10") int limit ) {
        final Criteria name = Criteria
                .where(ProductModel.NAME_ATTR).regex(".*" + q + ".*", "i");
        final Criteria desc = Criteria
                .where(ProductModel.DESCRIPTION_ATTR).regex(".*" + q + ".*", "i");
        final Query searchQuery = new Query().addCriteria(new Criteria().orOperator(name, desc));

        searchQuery.limit(limit);

        return mongoTemplate.find(searchQuery, ProductModel.class);
    }
}
