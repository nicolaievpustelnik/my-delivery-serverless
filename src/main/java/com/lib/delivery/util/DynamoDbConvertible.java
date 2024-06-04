package com.lib.delivery.util;

import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * This class contains useful methods that will do the variable type convertion from the AttributeValue
 */
public interface DynamoDbConvertible<T> {

    Map<String, AttributeValue> toAttributeValueMap(T obj);

    T fromAttributeValueMap(Map<String, AttributeValue> attributeValueMap);

    default void putIfNonNull(Map<String, AttributeValue> map, String key, String value) {
        if (value != null) {
            map.put(key, AttributeValue.builder().s(value).build());
        }
    }

    default void putIfNonNull(Map<String, AttributeValue> map, String key, List<String> value) {
        if (value != null) {
            map.put(key, AttributeValue.builder().ss(value).build());
        }
    }

    default void putIfNonNull(Map<String, AttributeValue> map, String key, Number value) {
        if (value != null) {
            map.put(key, AttributeValue.builder().n(value.toString()).build());
        }
    }

    default void putIfNonNull(Map<String, AttributeValue> map, String key, Boolean value) {
        if (value != null) {
            map.put(key, AttributeValue.builder().bool(value).build());
        }
    }

    default String getStringOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        return value != null ? value.s() : null;
    }

    default List<AttributeValue> getListOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        return value != null ? value.l() : null;
    }

    default Integer getIntOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        return value != null ? Integer.parseInt(value.n()) : null;
    }

    default Boolean getBoolOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        return value != null ? value.bool() : null;
    }

    default Instant getInstantOrNull(Map<String, AttributeValue> map, String key) {
        String value = getStringOrNull(map, key);
        return value != null ? Instant.parse(value) : null;
    }

    default List<String> getStringListOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        return value != null ? value.ss() : null;
    }

    default List<Integer> getIntListOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        if (value != null && value.ns() != null) {
            return value.ns().stream().map(Integer::parseInt).toList();
        }
        return Collections.emptyList();
    }

    default Map<String, AttributeValue> getMapOrNull(Map<String, AttributeValue> map, String key) {
        AttributeValue value = map.get(key);
        return value != null ? value.m() : null;
    }

    default LocalDateTime getDateOrNull(Map<String, AttributeValue> map, String key) {
        String value = getStringOrNull(map, key);
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
        return LocalDateTime.parse( value , dateFormatter );
    }

}


