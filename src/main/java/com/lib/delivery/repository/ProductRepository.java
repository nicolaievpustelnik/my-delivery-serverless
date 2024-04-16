package com.lib.delivery.repository;

import com.lib.delivery.entity.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

/**
 * Repository class for managing the persistence and retrieval of {@link Product} objects.
 */

@Repository
@RequiredArgsConstructor
@Slf4j
public class ProductRepository {

    private final DynamoDbClient dynamoDbClient;

    /**
     * Saves a {@link Product} object to the DynamoDB using the provided  to enforce uniqueness of the id.
     *
     * @param product            The {@link Product} object to be saved.
     */
    public Product save(Product product) {

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(Product.TABLE_NAME)
                .item(Product.DynamoDbConverter.i.toAttributeValueMap(product))
                .build();
        dynamoDbClient.putItem(putItemRequest);

        return Product.DynamoDbConverter.i.fromAttributeValueMap(putItemRequest.item());
    }

}
