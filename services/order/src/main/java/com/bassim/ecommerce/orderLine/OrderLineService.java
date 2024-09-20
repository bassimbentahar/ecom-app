package com.bassim.ecommerce.orderLine;

import com.bassim.ecommerce.order.OrderLine;
import com.bassim.ecommerce.order.OrderLineRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderLineService {

    private final OrderLineRepository repository;
    private final OrderLineMapper mapper;

    public OrderLineService(OrderLineRepository repository, OrderLineMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public Mono<Integer> saveOrderLine(OrderLineRequest request) {
        // Transformer la requête OrderLineRequest en entité OrderLine sans l'objet imbriqué Order
        OrderLine orderLine = new OrderLine(
                null,  // ID auto-généré
                request.orderId(),  // Utiliser l'ID de la commande au lieu de l'objet Order
                request.productId(),
                request.quantity()
        );

        // Sauvegarder l'entité OrderLine dans le repository de manière réactive
        return repository.save(orderLine)
                // Une fois sauvegardé, extraire l'ID de l'entité et le retourner dans un Mono<Integer>
                .map(OrderLine::getId);
    }

    public Flux<OrderLineResponse> findAllByOrderId(Integer orderId) {
        return repository.findAllByOrderId(orderId)
                .map(mapper::toOrderLineResponse);
    }
}
