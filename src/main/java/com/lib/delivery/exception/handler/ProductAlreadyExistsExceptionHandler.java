package com.lib.delivery.exception.handler;


import com.lib.delivery.exception.ProductAlreadyExistsException;
import com.toyota.exception.AbstractExceptionHandler;
import com.toyota.exception.ExceptionHandlerComponent;
import com.toyota.http.HttpStatus;
import com.toyota.http.ProblemDetail;
/**
 * This class is an exception handler for {@link ProductAlreadyExistsException}. It extends {@link AbstractExceptionHandler} and provides a custom
 * {@link ProblemDetail} to handle the {@link ProductAlreadyExistsException}.
 */
@ExceptionHandlerComponent
public class ProductAlreadyExistsExceptionHandler extends AbstractExceptionHandler<ProductAlreadyExistsException> {

    /**
     * Constructs a new {@code ProductAlreadyExistsExceptionHandler} instance.
     */
    public ProductAlreadyExistsExceptionHandler() {
        super(ProductAlreadyExistsException.class);
    }

    /**
     * Handles the {@link ProductAlreadyExistsException} and returns a {@link ProblemDetail} with the appropriate status, detail, and title.
     * @param e The {@link ProductAlreadyExistsException} to be handled.
     * @return A {@link ProblemDetail} instance containing the response information for the exception.
     */
    @Override
    public ProblemDetail handle(ProductAlreadyExistsException e) {
        return ProblemDetail.forStatusAndDetailAndTitle(HttpStatus.BAD_REQUEST, "Product already exists in the database",
            "Product already exists in the database");
    }
}
