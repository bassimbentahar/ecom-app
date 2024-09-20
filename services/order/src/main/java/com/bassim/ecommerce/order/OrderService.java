package com.bassim.ecommerce.order;

import com.bassim.ecommerce.customer.CustomerClient;
import com.bassim.ecommerce.kafka.OrderConfirmation;
import com.bassim.ecommerce.kafka.OrderProducer;
import com.bassim.ecommerce.orderLine.OrderLineService;
import com.bassim.ecommerce.payment.PaymentClient;
import com.bassim.ecommerce.payment.PaymentRequest;
import com.bassim.ecommerce.product.ProductClient;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class OrderService {

    private final CustomerClient customerClient;
    private final ProductClient productClient;
    private final  OrderRepository orderRepository;
    private final OrderMapper mapper;
    private final OrderLineService orderLineService;
    private final OrderProducer orderProducer;
    private final PaymentClient paymentClient;

    public OrderService(CustomerClient customerClient, ProductClient productClient, OrderRepository orderRepository, OrderMapper mapper, OrderLineService orderLineService, OrderProducer orderProducer, PaymentClient paymentClient) {
        this.customerClient = customerClient;
        this.productClient = productClient;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.orderLineService = orderLineService;
        this.orderProducer = orderProducer;
        this.paymentClient = paymentClient;
    }


    public Mono<Integer> createOrder(OrderRequest request) {
        // Étape 1 : Vérification du client
        return customerClient.findCustomerById(request.customerId())
                .switchIfEmpty(Mono.error(
                        new BusinessException("Cannot create order:: No customer exists with the provided ID:: " + request.customerId())))
                .flatMap(customer ->
                        productClient.purchaseProduct(request.products())
                                .collectList()
                                .flatMap(purchasedProducts ->
                                        orderRepository.save(mapper.toOrder(request))
                                                .flatMap(order -> {
                                                    // Créer un Flux de Mono pour la sauvegarde des lignes de commande
                                                    Flux<Mono<Integer>> orderLineMonos = Flux.fromIterable(request.products())
                                                            .map(purchaseRequest -> orderLineService.saveOrderLine(
                                                                    new OrderLineRequest(
                                                                            null,
                                                                            order.getId(),
                                                                            purchaseRequest.productId(),
                                                                            purchaseRequest.quantity()
                                                                    )
                                                            ));

                                                    // Attendre que toutes les lignes de commande soient sauvegardées
                                                    return Mono.when(orderLineMonos)
                                                            // Étape 2 : Demander le paiement une fois que les lignes de commande sont sauvegardées
                                                            .then(paymentClient.requestOrderPayment(new PaymentRequest(
                                                                    request.amount(),
                                                                    request.paymentMethod(),
                                                                    order.getId(),
                                                                    order.getReference(),
                                                                    customer
                                                            )))
                                                            // Étape 3 : Envoyer la confirmation de commande après le succès du paiement
                                                            .then(Mono.fromRunnable(() -> {
                                                                orderProducer.sendOrderConfirmation(new OrderConfirmation(
                                                                        request.reference(),
                                                                        request.amount(),
                                                                        request.paymentMethod(),
                                                                        customer,
                                                                        purchasedProducts
                                                                ));
                                                            }))
                                                            .thenReturn(order.getId()); // Retourner l'ID de la commande
                                                })
                                )
                );
    }


    public Flux<OrderResponse> findAll() {
        return orderRepository.findAll()
                .map(mapper::fromOrder);
    }

    public Mono<OrderResponse> findById(Integer id) {
        return orderRepository.findById(id)
                .map(mapper::fromOrder)
                .switchIfEmpty(Mono.error(
                        new EntityNotFoundException("No order found with the provided ID:: " +id )));
    }
}
