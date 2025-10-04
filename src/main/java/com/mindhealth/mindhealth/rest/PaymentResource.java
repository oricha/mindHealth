package com.mindhealth.mindhealth.rest;

import com.mindhealth.mindhealth.service.PaymentService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentResource {
    private final PaymentService payments;

    @Data
    static class IntentRequest { long amountCents; String currency; }

    @PostMapping("/intent")
    public ResponseEntity<String> intent(@RequestBody IntentRequest req) throws Exception {
        return ResponseEntity.ok(payments.createPaymentIntent(req.amountCents, req.currency));
    }

    @PostMapping("/confirm")
    public ResponseEntity<Void> confirm(@RequestParam String paymentIntentId) {
        payments.confirmPayment(paymentIntentId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/webhooks/stripe")
    public ResponseEntity<Void> webhook(@RequestBody String payload, @RequestHeader(name="Stripe-Signature", required=false) String signature) {
        // Placeholder: verify signature and handle events
        return ResponseEntity.ok().build();
    }
}
