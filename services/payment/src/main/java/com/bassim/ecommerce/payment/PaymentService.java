package com.bassim.ecommerce.payment;

import com.bassim.ecommerce.notification.NotificationProducer;
import com.bassim.ecommerce.notification.PaymentNotificationRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PaymentService {

    private final PaymentRepository repository;
    private final PaymentMapper mapper;
    private final NotificationProducer producer;

    public PaymentService(PaymentRepository repository, PaymentMapper mapper, NotificationProducer producer) {
        this.repository = repository;
        this.mapper = mapper;
        this.producer = producer;
    }

    public Mono<Integer> createPayment(PaymentRequest request) {
        var payment = repository.save(mapper.toPayment(request));

        producer.sendNotification(
                new PaymentNotificationRequest(
                        request.orderReference(),
                        request.amount(),
                        request.paymentMethod(),
                        request.customer().firstName(),
                        request.customer().lastName(),
                        request.customer().email()

                )
        );

        return payment.map(Payment::getId);
    }
}
