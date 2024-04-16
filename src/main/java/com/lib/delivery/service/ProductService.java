package com.lib.delivery.service;

import com.lib.delivery.entity.Product;
import com.lib.delivery.exception.ProductAlreadyExistsException;
import com.lib.delivery.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

/**
 * This class is responsible for handling the business logic related to the persistence and retrieval of {@link Product} objects using
 * {@link ProductRepository}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Saves a {@link Product} object to the DynamoDB, if the {@link Product} object already exist will update it, if it does not exist, it will create it.
     * @param product The {@link Product} object to be saved.
     * @return A new {@link Product} created.
     * @throws ProductAlreadyExistsException If a {@link Product} object with the same id already exists.
     */
    public Product save(Product product) {
        try {
            return productRepository.save(product);
        } catch (ConditionalCheckFailedException e) {
            throw new ProductAlreadyExistsException();
        }
    }

}
