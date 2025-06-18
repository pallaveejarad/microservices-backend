package com.payment.controller;
import com.payment.model.Payment;
import com.payment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public Payment doPayment(@RequestBody Payment payment) {
        return paymentService.savePayment(payment);
    }

    @GetMapping("/order/{orderId}")
    public Payment getPaymentDetails(@PathVariable Long orderId) {
        return paymentService.getPaymentDetailsByOrderId(orderId);
    }
}
