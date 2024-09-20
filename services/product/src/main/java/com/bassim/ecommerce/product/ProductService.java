package com.bassim.ecommerce.product;

import com.bassim.ecommerce.category.Category;
import com.bassim.ecommerce.category.CategoryRepository;
import com.bassim.ecommerce.exception.ProductPurchaseException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository repository;
    private final CategoryRepository categoryRepository; // Injection du CategoryRepository
    private final ProductMapper mapper;

    public ProductService(ProductRepository repository, CategoryRepository categoryRepository, ProductMapper mapper) {
        this.repository = repository;
        this.categoryRepository = categoryRepository;
        this.mapper = mapper;
    }

    public Mono<Integer> createProduct(@Valid ProductRequest request) {
        var product = mapper.toProduct(request);
        return repository.save(product)
                .map(Product::getId);
    }

    public Flux<ProductPurchaseResponse> purchaseProducts(List<ProductPurchaseRequest> requests) {
        var productIds = extractProductIds(requests);

        return repository.findAllByIdInOrderById(productIds)
                .collectList()
                .flatMapMany(storedProducts -> handleProductPurchases(requests, storedProducts));
    }

    private List<Integer> extractProductIds(List<ProductPurchaseRequest> requests) {
        return requests.stream()
                .map(ProductPurchaseRequest::productId)
                .toList();
    }

    private Flux<ProductPurchaseResponse> handleProductPurchases(List<ProductPurchaseRequest> requests, List<Product> storedProducts) {
        var productIds = extractProductIds(requests);
        validateProductExistence(productIds, storedProducts);
        var productMap = mapProductsById(storedProducts);

        return Flux.fromIterable(requests)
                .flatMap(request -> processSingleProductPurchase(request, productMap));
    }

    private void validateProductExistence(List<Integer> productIds, List<Product> storedProducts) {
        if (productIds.size() != storedProducts.size()) {
            var missingIds = identifyMissingProductIds(productIds, storedProducts);
            throw new ProductPurchaseException("Products not found: " + missingIds);
        }
    }

    private Set<Integer> identifyMissingProductIds(List<Integer> productIds, List<Product> storedProducts) {
        var storedProductIds = storedProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toSet());

        return productIds.stream()
                .filter(id -> !storedProductIds.contains(id))
                .collect(Collectors.toSet());
    }

    private Map<Integer, Product> mapProductsById(List<Product> storedProducts) {
        return storedProducts.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));
    }

    private Mono<ProductPurchaseResponse> processSingleProductPurchase(ProductPurchaseRequest request, Map<Integer, Product> productMap) {
        var product = productMap.get(request.productId());

        if (product.getAvailableQuantity() < request.quantity()) {
            return Mono.error(new ProductPurchaseException(
                    "Insufficient stock quantity for product with ID: " + request.productId()));
        }

        product.setAvailableQuantity(product.getAvailableQuantity() - request.quantity());

        return repository.save(product)
                .flatMap(savedProduct ->
                        categoryRepository.findById(savedProduct.getCategoryId()) // Charger la catégorie
                                .map(category -> mapper.toProductPurchaseResponse(savedProduct, request.quantity())) // Créer la réponse avec la catégorie
                );
    }

    public Mono<ProductResponse> findById(Integer productId) {
        return repository.findById(productId)
                .flatMap(product ->
                        categoryRepository.findById(product.getCategoryId()) // Charger la catégorie
                                .map(category -> mapper.toProductResponse(product, category)) // Créer la réponse avec la catégorie
                )
                .switchIfEmpty(Mono.error(new EntityNotFoundException("Product not found with ID: " + productId)));
    }

    public Flux<ProductResponse> findAll() {
        return repository.findAll()
                .flatMap(product ->
                        categoryRepository.findById(product.getCategoryId()) // Charger la catégorie
                                .map(category -> mapper.toProductResponse(product, category)) // Créer la réponse avec la catégorie
                )
                .switchIfEmpty(Mono.error(new EntityNotFoundException("No products found")));
    }
}
