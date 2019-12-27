package com.example.azure.persistence.models;

import lombok.*;
import org.springframework.data.mongodb.core.index.TextIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
@Document(collection = "products")
public class ProductModel extends ItemModel {

    public static final String NAME_ATTR = "name";
    public static final String DESCRIPTION_ATTR = "description";


    private String code;
    @TextIndexed
    private String name;
    private String description;
    private String manufacturer;
    private String merchant;
    private String asins;
    private String brand;
    private String currency;
    private List<String> sources;
    private List<String> categories;
    private List<String> tags;
    private List<ImageModel> images;
    private BigDecimal priceAmountMax;
    private BigDecimal priceAmountMin;
}
