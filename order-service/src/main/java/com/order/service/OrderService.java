package com.order.service;

import com.order.model.Order;
import com.order.model.payment.PaymentDTO;
import com.order.model.product.ProductDTO;
import com.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private WebClient.Builder webClientBuilder;


    public Order placeOrder(Order order) {

        // call product service to check if product is present or not
        ProductDTO productResponse = getProductDetails(order.getProductId());

        if (productResponse == null) {
            throw new RuntimeException("Product not in stock");
        }

        order.setOrderStatus("PLACED");
        Order savedOrder = orderRepository.save(order);

       PaymentDTO paymentResponse =  initiatePayment(savedOrder);


        // call payment service - old approach using Rest Template
        // PaymentDTO paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/api/payment",paymentReq,PaymentDTO.class);

        return savedOrder;
    }

    @CircuitBreaker(name= "paymentBreaker", fallbackMethod = "handlePaymentFailure")
    private PaymentDTO initiatePayment(Order savedOrder) {
        //Create a PaymentRequest
        PaymentDTO paymentRequest = PaymentDTO.builder()
                .orderId(savedOrder.getId())
                .paymentStatus("Success")
                .transactionId(UUID.randomUUID().toString())
                .amount(savedOrder.getPrice())
                .build();

        PaymentDTO paymentResponse = webClientBuilder.build()
                .post()
                .uri("http://PAYMENT-SERVICE/api/payment")
                .bodyValue(paymentRequest)
                .retrieve()
                .onStatus(status->status.is4xxClientError()|| status.is5xxServerError(),clientResponse -> {
                    return clientResponse
                            .bodyToMono(String.class)
                            .map(errorBody-> new RuntimeException("Payment Service is currently unavailable"));
                })
                .bodyToMono(PaymentDTO.class)
                .block();
        return paymentResponse;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @CircuitBreaker(name= "ProductBreaker", fallbackMethod = "handleProductFailure")
    public ProductDTO getProductDetails(String productId) {
        return webClientBuilder.build().get().uri("http://PRODUCT-SERVICE/api/products/" + productId)
                .retrieve()
                .onStatus(status->status.is4xxClientError()|| status.is5xxServerError(),clientResponse -> {
                    return clientResponse
                            .bodyToMono(String.class)
                            .map(errorBody-> new RuntimeException("Product Service is currently unavailable"));
                })
                .bodyToMono(ProductDTO.class)
                .block();
    }

    //fallback method
    public Order handlePaymentFailure(Order order,Exception ex){
        System.out.println("fallback method called due to : "+ex);
        order.setOrderStatus("Payment Pending");
        return orderRepository.save(order);
    }

    public String handleProductFailure(Exception ex){
        System.out.println("fallback method called due to : "+ex);
      return ex.getMessage();
    }

}

