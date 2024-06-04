package com.lib.delivery.function;


import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lib.delivery.dto.response.OrderResponse;
import com.lib.delivery.dto.request.OrderRequest;
import com.lib.delivery.entity.Order;
import com.lib.delivery.mapper.OrderMapper;
import com.lib.delivery.service.OrderService;
import com.toyota.http.HttpStatus;
import com.toyota.utils.RequestUtils;
import com.toyota.utils.ResponseUtils;
import com.toyota.validation.FieldValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component("orders")
@RequiredArgsConstructor
public class Orders implements Function<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final FieldValidator fieldValidator;
    private final OrderMapper orderMapper;
    private final ObjectMapper objectMapper;

    @Value("${PENDING_ORDERS_QUEUE}")
    private String queueUrl;

    private static final Logger LOG = LoggerFactory.getLogger(Create.class);

    @SneakyThrows
    @Override
    public APIGatewayProxyResponseEvent apply(APIGatewayProxyRequestEvent requestEvent) {
        OrderRequest orderRequest = RequestUtils.parseJsonBody(requestEvent, OrderRequest.class);
        LOG.info("Received: {}", orderRequest);
        fieldValidator.validate(orderRequest);
        Order order = orderMapper.map(orderRequest);
        String message = sendToQueue(order);
        LOG.info("SQS " +
                "Message: {}", message);
        OrderResponse orderResponse = orderMapper.map(order);

        return ResponseUtils.createResponse(orderResponse, HttpStatus.CREATED);
    }

    private String sendToQueue(Order order) throws JsonProcessingException {
        AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();
        String messageBody = objectMapper.writeValueAsString(order);
        SendMessageRequest sendMessageRequest = new SendMessageRequest()
                .withQueueUrl(queueUrl)
                .withMessageBody(messageBody);
        SendMessageResult sendMessageResult = sqs.sendMessage(sendMessageRequest);
        return sendMessageResult.getMessageId();
    }
}
