

-- Création de la table customer_order si elle n'existe pas
CREATE TABLE IF NOT EXISTS customer_order (
                                              id SERIAL PRIMARY KEY, -- clé primaire avec auto-incrément
                                              reference VARCHAR(255) NOT NULL, -- référence de la commande
                                              total_amount NUMERIC(19, 2) NOT NULL, -- montant total de la commande
                                              payment_method VARCHAR(50) NOT NULL, -- méthode de paiement (doit correspondre à l'énumération PaymentMethod)
                                              customer_id VARCHAR(255) NOT NULL, -- identifiant du client
                                              create_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP, -- date de création
                                              last_modified_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- date de dernière modification
);

-- Création de la table order_line si elle n'existe pas
CREATE TABLE IF NOT EXISTS order_line (
                                          id SERIAL PRIMARY KEY, -- clé primaire avec auto-incrément
                                          order_id INTEGER NOT NULL, -- clé étrangère vers la table customer_order
                                          product_id INTEGER NOT NULL, -- identifiant du produit
                                          quantity DOUBLE PRECISION NOT NULL, -- quantité du produit
                                          CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES customer_order(id) -- contrainte de clé étrangère
);
