package com.bassim.ecommerce.payment;

import com.bassim.ecommerce.order.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

@Service
public class PaymentClient {

    private static final Logger logger = LoggerFactory.getLogger(PaymentClient.class);

    @Value("${application.config.payment-url}")
    private String paymentUrl;

    private final WebClient webClient;

    public PaymentClient(WebClient webClient) {
        this.webClient = webClient;
    }


    public Mono<Integer> requestOrderPayment(PaymentRequest request){
        return webClient
                .post()
                .uri(paymentUrl)
                .header(CONTENT_TYPE, APPLICATION_JSON) // Ajouter l'en-tête Content-Type
                .body(BodyInserters.fromValue(request))
                .retrieve()
                .bodyToMono(Integer.class)
                .onErrorResume(WebClientResponseException.class, e -> {
                    // Gérer les erreurs HTTP spécifiques (4xx, 5xx)
                    logger.error("Error occurred while calling the API: HTTP {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    return Mono.error(new BusinessException("Error occurred while retrieving the customer data " + e.getStatusCode()));
                })
                .onErrorResume(Exception.class, e -> {
                    // Gérer d'autres types d'erreurs
                    logger.error("Unexpected error occurred while calling the API", e);
                    return Mono.error(new BusinessException("Unexpected error occurred while calling the API"));
                });
    }

}
