package com.lib.delivery.exception;

/**
 * The {@code ProductAlreadyExistsException} is a custom exception class that represents a situation where an Product already exists in the database.
 * This exception is used to provide a more specific error message and can be caught and handled by custom exception handlers to return an appropriate
 * response to the client.
 */
public class ProductAlreadyExistsException extends RuntimeException {


    /**
     * Constructor that creates a new {@code ProductAlreadyExistsException} with a default error message.
     */
    public ProductAlreadyExistsException() {
        super("Product already exists in the database");
    }

}
