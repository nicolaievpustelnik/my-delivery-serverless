package com.lib.delivery.entity;

import com.lib.delivery.entity.enums.OrderStatus;
import com.lib.delivery.util.DynamoDbConvertible;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.HashMap;
import java.util.List;
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
public class Order {

    public static final String TABLE_NAME = "restaurant-orders";

    private String id;
    private String clientName;
    private List<String> products;
    private String status;

    @NoArgsConstructor(access = PRIVATE)
    public static class DynamoDbConverter implements DynamoDbConvertible<Order> {

        public static final DynamoDbConvertible<Order> i = new DynamoDbConverter();

        public Map<String, AttributeValue> toAttributeValueMap(Order order) {
            Map<String, AttributeValue> attributeValueMap = new HashMap<>();

            if (Objects.nonNull(order)) {
                putIfNonNull(attributeValueMap, "id", order.getId());
                putIfNonNull(attributeValueMap, "clientName", order.getClientName());
                putIfNonNull(attributeValueMap, "products", order.getProducts());
                putIfNonNull(attributeValueMap, "status", OrderStatus.PENDING.name());
            }

            return attributeValueMap;
        }

        public Order fromAttributeValueMap(Map<String, AttributeValue> attributeValueMap) {
            Order order = new Order();
            order.setId(getStringOrNull(attributeValueMap, "id"));
            order.setClientName(getStringOrNull(attributeValueMap, "clientName"));
            order.setProducts(getStringListOrNull(attributeValueMap, "products"));
            order.setStatus(getStringOrNull(attributeValueMap, "status"));
            return order;
        }
    }
}