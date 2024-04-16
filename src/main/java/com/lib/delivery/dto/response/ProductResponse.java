package com.lib.delivery.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.lib.delivery.entity.Product;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * The dto class {@code ProductResponse} represents a {@link Product}.
 */
@Builder
public record ProductResponse(String id,
                              String group,
                              String description,
                              String ingredients,
                              @JsonSerialize(using = LocalDateTimeSerializer.class)
                              @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                              @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                              LocalDateTime creationDate) {

}