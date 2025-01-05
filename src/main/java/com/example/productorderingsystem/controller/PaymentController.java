package com.example.productorderingsystem.controller;

import com.example.productorderingsystem.entity.Payment;
import com.example.productorderingsystem.service.PaymentService;
import com.example.productorderingsystem.service.StripePaymentService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final StripePaymentService stripePaymentService;
    private final PaymentService paymentService;
    public PaymentController(StripePaymentService stripePaymentService, PaymentService paymentService) {
        this.stripePaymentService = stripePaymentService;
        this.paymentService = paymentService;
    }

    @PostMapping("/charge")
    public Map<String, Object> chargeCard(@RequestBody Map<String, Object> paymentDetails) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Extract details from paymentDetails
            Long amount = Long.parseLong(paymentDetails.get("amount").toString());
            String currency = paymentDetails.get("currency").toString();
            String paymentMethodId = paymentDetails.get("paymentMethodId").toString();

            // Create a payment intent with Stripe
            PaymentIntent paymentIntent = stripePaymentService.createPaymentIntentWithMethod(amount, currency, paymentMethodId);

            // Save payment details in the database
            Payment payment = new Payment();
            payment.setAmount(BigDecimal.valueOf(amount / 100.0)); // Convert cents to dollars
            payment.setMethod("card");
            payment.setStatus(paymentIntent.getStatus());
            paymentService.savePayment(payment);

            response.put("status", "success");
            response.put("paymentIntent", paymentIntent);
        } catch (StripeException e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", "Invalid payment details");
        }
        return response;
    }
}
