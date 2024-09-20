package com.bassim.ecommerce.product;

import com.bassim.ecommerce.order.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Flux;

import java.util.List;

import static jakarta.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.apache.http.HttpHeaders.CONTENT_TYPE;

@Service
public class ProductClient {

    private static final Logger logger = LoggerFactory.getLogger(ProductClient.class);

    @Value("${application.config.product-url}")
    private String productUrl;

    private final WebClient webClient;

    public ProductClient(WebClient webClient) {
        this.webClient = webClient;
    }

    public Flux<PurchaseResponse> purchaseProduct(List<PurchaseRequest> requestBody) {
        return webClient.post()
                .uri(productUrl + "/purchase") // Construire l'URL complète
                .header(CONTENT_TYPE, APPLICATION_JSON) // Ajouter l'en-tête Content-Type
                .bodyValue(requestBody) // Ajouter le corps de la requête
                .retrieve()
                .bodyToFlux(PurchaseResponse.class) // Récupérer la réponse comme flux réactif
                .onErrorResume(WebClientResponseException.class, e -> {
                    // Gérer les erreurs HTTP spécifiques (4xx, 5xx)
                    logger.error("Error occurred while calling the API: HTTP {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
                    return Flux.error(new BusinessException("Error occurred while processing the product purchase "+ e.getStatusCode()));
                })
                .onErrorResume(Exception.class, e -> {
                    // Gérer d'autres types d'erreurs
                    logger.error("Erreur inattendue lors de l'appel à l'API", e);
                    return Flux.error(new BusinessException("Erreur inattendue lors de l'appel à l'API"));
                });
    }
}
