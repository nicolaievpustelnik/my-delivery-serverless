package com.lib.delivery.function;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.delivery.entity.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

import java.util.Map;

@Slf4j
@Component("prepareOrder")
@RequiredArgsConstructor
public class PrepareOrder implements RequestHandler<SQSEvent, Void> {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger LOG = LogManager.getLogger(PrepareOrder.class);
    private final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    @Override
    public Void handleRequest(SQSEvent event, Context context) {
        event.getRecords().forEach(record -> {
            String message = record.getBody();
            LOG.info("Message received: {}", message);
            try {
                Order order = objectMapper.readValue(message, Order.class);
                LOG.info("Order: {}", order);
                saveOrder(order);
            } catch (JsonProcessingException e) {
                LOG.error("Failed to parse SQS message: {}", e.getMessage(), e);
            }
        });
        return null;
    }

    private void saveOrder(Order order) {
        Map<String, AttributeValue> item = Order.DynamoDbConverter.i.toAttributeValueMap(order);
        PutItemRequest putItemRequest = PutItemRequest.builder()
                .tableName(Order.TABLE_NAME)
                .item(item)
                .build();
        dynamoDbClient.putItem(putItemRequest);
        LOG.info("Order saved to DynamoDB: {}", order);
    }
}