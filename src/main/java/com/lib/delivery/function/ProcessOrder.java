package com.lib.delivery.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.DynamodbEvent;
import com.lib.delivery.entity.Order;
import com.lib.delivery.entity.enums.OrderStatus;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;

import java.util.HashMap;
import java.util.Map;


public class ProcessOrder implements RequestHandler<DynamodbEvent, Void> {

    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    @Override
    public Void handleRequest(DynamodbEvent dynamodbEvent, Context context) {
        for (DynamodbEvent.DynamodbStreamRecord record : dynamodbEvent.getRecords()) {
            if (record.getEventName().equals("INSERT")) {
                String orderId = record.getDynamodb().getNewImage().get("id").getS();
                updateOrderStatus(orderId, OrderStatus.PROCESSING.name());
            }
        }
        return null;
    }

    private void updateOrderStatus(String orderId, String newStatus) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("id", AttributeValue.builder().s(orderId).build());

        Map<String, AttributeValue> attributeValues = new HashMap<>();
        attributeValues.put(":newStatus", AttributeValue.builder().s(newStatus).build());

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(Order.TABLE_NAME)
                .key(key)
                .updateExpression("SET #status = :newStatus")
                .expressionAttributeNames(Map.of("#status", "status"))
                .expressionAttributeValues(attributeValues)
                .build();

        dynamoDbClient.updateItem(request);
    }
}