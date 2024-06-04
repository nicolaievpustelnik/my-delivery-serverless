package com.lib.delivery.function;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lib.delivery.dto.request.RestaurantRequest;
import com.lib.delivery.dto.response.ProductResponse;
import com.lib.delivery.entity.Product;
import com.lib.delivery.entity.Restaurant;
import com.lib.delivery.mapper.ProductMapper;
import com.lib.delivery.mapper.RestaurantMapper;
import com.lib.delivery.service.ProductService;
import com.toyota.http.HttpStatus;
import com.toyota.utils.RequestUtils;
import com.toyota.utils.ResponseUtils;
import com.toyota.validation.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class handles the creation of {@link Product} objects.
 */
@Slf4j
@Component("create")
@RequiredArgsConstructor
public class Create implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final FieldValidator fieldValidator;
    private final ProductMapper productMapper;
    private final RestaurantMapper restaurantMapper;
    private final ProductService productService;

    private static final Logger LOG = LogManager.getLogger(Create.class);

    /**
     * Handles the incoming {@link APIGatewayProxyRequestEvent} and returns an {@link APIGatewayProxyResponseEvent} containing the response. The
     * method converts the request body to a {@link RestaurantRequest} object, validates the fields, maps it to a {@link Product} object, saves it using
     * {@link ProductService}, and creates a response with the appropriate status code and body.
     * @param requestEvent The incoming {@link APIGatewayProxyRequestEvent}.
     * @return The {@link APIGatewayProxyResponseEvent} containing the response.
     */
    @SneakyThrows
    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        RestaurantRequest restaurantRequest = RequestUtils.parseJsonBody(requestEvent, RestaurantRequest.class);
        LOG.info("Received: {}", restaurantRequest);
        fieldValidator.validate(restaurantRequest);
        Restaurant restaurant = restaurantMapper.map(restaurantRequest);

        List<ProductResponse> productResponses = restaurant.getProducts().stream()
                .map(productMapper::map)
                .collect(Collectors.toList());

        restaurant.getProducts().forEach(productService::save);

        return ResponseUtils.createResponse(productResponses, HttpStatus.CREATED);
    }

}
