package com.lib.delivery.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Builder
public record ProductRequest(@NotBlank(message = "Id must not be null or empty.")
                             String id,
                             @Size(min = 3, max = 20)
                             @NotBlank(message = "Group must not be null or empty.")
                             String group,
                             @NotBlank(message = "Description must not be null or empty.")
                             String description,
                             @NotBlank(message = "Ingredients must not be null or empty.")
                             String ingredients,
                             @NotNull(message = "Creation Date must not be null or empty.")
                             @JsonSerialize(using = LocalDateTimeSerializer.class)
                             @JsonDeserialize(using = LocalDateTimeDeserializer.class)
                             @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
                             LocalDateTime creationDate) {

}