-- Insertion de catégories
INSERT INTO category (id, description, name)
VALUES
    (nextval('category_seq'), 'Electronic devices and gadgets', 'Electronics'),
    (nextval('category_seq'), 'Books of various genres', 'Books'),
    (nextval('category_seq'), 'Clothing and accessories', 'Apparel'),
    (nextval('category_seq'), 'Home and kitchen appliances', 'Home & Kitchen'),
    (nextval('category_seq'), 'Sports equipment and accessories', 'Sports'),
    (nextval('category_seq'), 'Health and beauty purchaseResponses', 'Health & Beauty');

-- Insertion de produits
INSERT INTO product (id, description, name, available_quantity, price, category_id)
VALUES
    (nextval('product_seq'), 'Latest smartphone with advanced features', 'Smartphone X', 100, 999.99, (SELECT id FROM category WHERE name = 'Electronics')),
    (nextval('product_seq'), '4K Ultra HD Smart TV', 'Smart TV 55"', 50, 1299.99, (SELECT id FROM category WHERE name = 'Electronics')),
    (nextval('product_seq'), 'Science fiction novel', 'Dune', 200, 19.99, (SELECT id FROM category WHERE name = 'Books')),
    (nextval('product_seq'), 'Bestselling mystery novel', 'The Da Vinci Code', 150, 14.99, (SELECT id FROM category WHERE name = 'Books')),
    (nextval('product_seq'), 'Comfortable cotton t-shirt', 'Plain T-Shirt', 300, 9.99, (SELECT id FROM category WHERE name = 'Apparel')),
    (nextval('product_seq'), 'Running shoes with cushioned soles', 'Running Shoes', 80, 89.99, (SELECT id FROM category WHERE name = 'Sports')),
    (nextval('product_seq'), 'Powerful blender for smoothies', 'Blender Pro', 70, 129.99, (SELECT id FROM category WHERE name = 'Home & Kitchen')),
    (nextval('product_seq'), 'Organic face moisturizer', 'Moisturizing Cream', 120, 29.99, (SELECT id FROM category WHERE name = 'Health & Beauty')),
    (nextval('product_seq'), 'High-definition action camera', 'Action Camera', 40, 199.99, (SELECT id FROM category WHERE name = 'Electronics')),
    (nextval('product_seq'), 'Durable stainless steel knife set', 'Knife Set', 60, 79.99, (SELECT id FROM category WHERE name = 'Home & Kitchen'));
