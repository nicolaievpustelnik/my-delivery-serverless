package com.lib.delivery;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.lib.delivery.dto.request.ProductRequest;
import com.lib.delivery.dto.request.RestaurantRequest;
import com.lib.delivery.dto.response.ProductResponse;
import com.toyota.http.ProblemDetail;
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RegisterReflectionForBinding({
        APIGatewayProxyRequestEvent.class,
        APIGatewayProxyRequestEvent.ProxyRequestContext.class,
        APIGatewayProxyRequestEvent.RequestIdentity.class,
        APIGatewayProxyResponseEvent.class,
        ProblemDetail.class,
        ProductRequest.class,
        RestaurantRequest.class,
        ProductResponse.class
})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
