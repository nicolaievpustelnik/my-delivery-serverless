package com.lib.delivery.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.util.List;

@Builder
public record OrderRequest(@NotBlank(message = "Id must not be null or empty.")
                            String id,
                           @NotBlank(message = "ClientName must not be null or empty.")
                            String clientName,
                            List<String> products) {

}