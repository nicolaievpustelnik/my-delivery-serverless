package com.lib.delivery.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The {@code Restaurant}
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class Restaurant {

    private String name;
    private List<Product> products;
}