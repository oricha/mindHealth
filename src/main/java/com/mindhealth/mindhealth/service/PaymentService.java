package com.mindhealth.mindhealth.service;

import com.stripe.Stripe;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {

    @Value("${stripe.api-key:}")
    private String apiKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = apiKey;
    }

    public String createPaymentIntent(long amountCents, String currency) throws Exception {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setAmount(amountCents)
                .setCurrency(currency)
                .build();
        PaymentIntent intent = PaymentIntent.create(params);
        return intent.getClientSecret();
    }

    public boolean confirmPayment(String paymentIntentId) {
        // Placeholder: Real confirmation is from client + webhook
        return true;
    }
}

