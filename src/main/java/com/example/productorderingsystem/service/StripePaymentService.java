// package com.example.productorderingsystem.service;

// import com.stripe.exception.StripeException;
// import com.stripe.model.PaymentIntent;
// import com.stripe.param.PaymentIntentCreateParams;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Service
// public class StripePaymentService {

//     @Value("${stripe.api.key}")
//     private String stripeApiKey;

//     public String createPaymentIntent(long amountInCents, String currency, String description) throws StripeException {
//         // Set Stripe API key
//         com.stripe.Stripe.apiKey = stripeApiKey;

//         // Create payment intent parameters
//         PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
//                 .setAmount(amountInCents)
//                 .setCurrency(currency)
//                 .setDescription(description)
//                 .build();

//         // Create payment intent
//         PaymentIntent paymentIntent = PaymentIntent.create(params);
//         return paymentIntent.getClientSecret();
//     }
// }
package com.example.productorderingsystem.service;

import com.example.productorderingsystem.entity.Payment;
import com.example.productorderingsystem.entity.Order;
import com.example.productorderingsystem.repository.PaymentRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class StripePaymentService {

    private final PaymentRepository paymentRepository;

    @Value("${stripe.api.key}")
    private String stripeApiKey;

    public Payment processPayment(BigDecimal amount, String currency, String paymentMethod, Order order) throws StripeException {
        Stripe.apiKey = stripeApiKey;

        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount.multiply(BigDecimal.valueOf(100)).longValue()); // Stripe accepts amounts in cents
        params.put("currency", currency);
        params.put("payment_method", paymentMethod);
        params.put("confirm", true);

        PaymentIntent paymentIntent = PaymentIntent.create(params);

        Payment payment = new Payment();
        payment.setAmount(amount);
        payment.setMethod("Stripe");
        payment.setStatus(paymentIntent.getStatus());
        payment.setOrder(order);

        return paymentRepository.save(payment);
    }

    public PaymentIntent createPaymentIntentWithMethod(Long amount, String currency, String paymentMethodId) throws StripeException {
        Map<String, Object> params = new HashMap<>();
        params.put("amount", amount);
        params.put("currency", currency);
        params.put("payment_method", paymentMethodId);
        params.put("confirmation_method", "manual");
        params.put("confirm", true);
    
        return PaymentIntent.create(params);
    }
    
}
