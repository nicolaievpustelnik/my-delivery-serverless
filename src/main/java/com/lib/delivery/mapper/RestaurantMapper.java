package com.lib.delivery.mapper;

import com.lib.delivery.dto.request.RestaurantRequest;
import com.lib.delivery.dto.response.ProductResponse;
import com.lib.delivery.entity.Restaurant;
import org.mapstruct.Mapper;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING
)
public interface RestaurantMapper {

    /**
     * Maps a ProductRequest to a Restaurant.
     * @param restaurantRequest The RestaurantRequest to be mapped.
     * @return The mapped Restaurant.
     */
    Restaurant map(RestaurantRequest restaurantRequest);

    /**
     * Maps a Restaurant to a ProductResponse.
     * @param restaurant The Restaurant to be mapped.
     * @return The mapped ProductResponse.
     */
    ProductResponse map(Restaurant restaurant);
}

