package com.bassim.ecommerce.kafka;

import com.bassim.ecommerce.email.EmailService;
import com.bassim.ecommerce.kafka.order.OrderConfirmation;
import com.bassim.ecommerce.kafka.payment.PaymentConfirmation;
import com.bassim.ecommerce.notification.Notification;
import com.bassim.ecommerce.notification.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.bassim.ecommerce.notification.NotificationType.ORDER_CONFIRMATION;
import static com.bassim.ecommerce.notification.NotificationType.PAYMENT_CONFIRMATION;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {
    private final NotificationRepository repository;
    private final EmailService emailService;


    @KafkaListener(topics = "payment-topic")
    public void consumePaymentSuccessNotification(PaymentConfirmation paymentConfirmation){
        log.info(format("Consuming the message from payment-topic:: %s", paymentConfirmation));
        repository.save(
                Notification.builder()
                        .notificationType(PAYMENT_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .paymentConfirmation(paymentConfirmation)
                        .build()
        );

        var customerName = String.format("%s %s",
                        Optional.ofNullable(paymentConfirmation.customerFirstname()).orElse(""),
                        Optional.ofNullable(paymentConfirmation.customerLastname()).orElse(""))
                .trim();

        emailService.sendPaymentSuccessEmail(
                                                paymentConfirmation.customerEmail(),
                                                customerName,
                                                paymentConfirmation.amount(),
                                                paymentConfirmation.orderReference()
                                                );
    }

    @KafkaListener(topics = "order-topic")
    public void consumeOrderConfirmationSuccessNotification(OrderConfirmation orderConfirmation){
        log.info(format("Consuming the message from order-topic:: %s", orderConfirmation));
        repository.save(
                Notification.builder()
                        .notificationType(ORDER_CONFIRMATION)
                        .notificationDate(LocalDateTime.now())
                        .orderConfirmation(orderConfirmation)
                        .build()
        );

        var customerName = String.format("%s %s",
                        Optional.ofNullable(orderConfirmation.customer().firstName()).orElse(""),
                        Optional.ofNullable(orderConfirmation.customer().lastName()).orElse(""))
                .trim();

        emailService.sendOrderConfirmationEmail(
                orderConfirmation.customer().email(),
                customerName,
                orderConfirmation.totalAmount(),
                orderConfirmation.orderReference(),
                orderConfirmation.products()
        );
    }
}
