CREATE TABLE IF NOT EXISTS payment (
    id SERIAL PRIMARY KEY,  -- SERIAL auto-incrément pour PostgreSQL
    order_id BIGINT NOT NULL,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_id VARCHAR(100),
    create_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_modified_date TIMESTAMP
    );
