package com.lib.delivery.dto.response;

import com.lib.delivery.entity.Product;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderResponse(@NotBlank(message = "Id must not be null or empty.")
                            String id,
                            @NotBlank(message = "ClientName must not be null or empty.")
                            String clientName,
                            @NotBlank(message = "Products must not be null or empty.")
                            List<String> products) {

}