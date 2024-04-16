package com.lib.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.util.List;

@Builder
public record RestaurantRequest(@Size(min = 3, max = 20)
                                @NotBlank(message = "Name must not be null or empty.")
                                String name,
                                List<ProductRequest> products) {

}
