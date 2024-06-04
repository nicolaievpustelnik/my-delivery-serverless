package com.lib.delivery.repository;

import com.lib.delivery.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

@Repository
@RequiredArgsConstructor
@Slf4j
public class OrderRepository {

    private final DynamoDbClient dynamoDbClient;

    public Order save(Order order) {

        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(Order.TABLE_NAME)
                .item(Order.DynamoDbConverter.i.toAttributeValueMap(order))
                .build();
        dynamoDbClient.putItem(putItemRequest);

        return Order.DynamoDbConverter.i.fromAttributeValueMap(putItemRequest.item());
    }
}
