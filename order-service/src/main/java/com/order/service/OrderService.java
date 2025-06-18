package com.order.service;

import com.order.model.Order;
import com.order.model.payment.PaymentDTO;
import com.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RestTemplate restTemplate;


    public Order placeOrder(Order order) {
        order.setOrderStatus("PLACED");
        Order savedOrder = orderRepository.save(order);

        //build payment request
        PaymentDTO paymentReq = PaymentDTO.builder().orderId(savedOrder.getId()).amount(savedOrder.getPrice()).paymentStatus("success").transactionId(UUID.randomUUID().toString()).build();

        //call payment service
        PaymentDTO paymentResponse = restTemplate.postForObject("http://PAYMENT-SERVICE/api/payment",paymentReq,PaymentDTO.class);
        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }



}

