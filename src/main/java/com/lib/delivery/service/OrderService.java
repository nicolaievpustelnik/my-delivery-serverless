package com.lib.delivery.service;

import com.lib.delivery.entity.Order;
import com.lib.delivery.entity.Product;
import com.lib.delivery.exception.ProductAlreadyExistsException;
import com.lib.delivery.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.dynamodb.model.ConditionalCheckFailedException;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    public Order save(Order order) {
        try {
            return orderRepository.save(order);
        } catch (ConditionalCheckFailedException e) {
            throw new ProductAlreadyExistsException();
        }
    }

}
