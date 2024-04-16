package com.lib.delivery.entity;

import com.lib.delivery.util.DynamoDbConvertible;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static lombok.AccessLevel.PRIVATE;

/**
 * The {@code Product}
 * That information is stored in the DynamoDB table for further uses.
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class Product {

    public static final String TABLE_NAME = "restaurant-products";

    private String id;
    private String group;
    private String description;
    private String ingredients;
    private LocalDateTime creationDate;

    @NoArgsConstructor(access = PRIVATE)
    public static class DynamoDbConverter implements DynamoDbConvertible<Product> {

        public static final DynamoDbConvertible<Product> i = new DynamoDbConverter();

        public Map<String, AttributeValue> toAttributeValueMap(Product product) {
            Map<String, AttributeValue> attributeValueMap = new HashMap<>();

            if (Objects.nonNull(product)) {
                putIfNonNull(attributeValueMap, "id", product.getId());
                putIfNonNull(attributeValueMap, "group", product.getGroup());
                putIfNonNull(attributeValueMap, "description", product.getDescription());
                putIfNonNull(attributeValueMap, "ingredients", product.getIngredients());
                putIfNonNull(attributeValueMap, "creationDate", product.getCreationDate().toString());
            }

            return attributeValueMap;
        }

        public  Product fromAttributeValueMap(Map<String, AttributeValue> attributeValueMap) {
            Product product = new Product();
            product.setId(getStringOrNull(attributeValueMap, "id"));
            product.setGroup(getStringOrNull(attributeValueMap, "group"));
            product.setDescription(getStringOrNull(attributeValueMap, "description"));
            product.setIngredients(getStringOrNull(attributeValueMap, "ingredients"));
            product.setCreationDate(getDateOrNull(attributeValueMap, "creationDate"));
            return product;
        }

    }
}