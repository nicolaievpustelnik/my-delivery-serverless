package com.lib.delivery.mapper;

import com.lib.delivery.dto.request.ProductRequest;
import com.lib.delivery.dto.response.ProductResponse;
import com.lib.delivery.entity.Product;
import org.mapstruct.Mapper;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(
        componentModel = SPRING
)
public interface ProductMapper {

    /**
     * Maps a Product to a ProductResponse.
     * @param product The Product to be mapped.
     * @return The mapped ProductResponse.
     */
    ProductResponse map(Product product);

}
