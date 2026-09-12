CREATE TABLE orders (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    client_id UUID NOT NULL REFERENCES users(id),
    traiteur_id UUID NOT NULL REFERENCES traiteurs(id),
    status VARCHAR(50) NOT NULL,
    delivery_address VARCHAR(500) NOT NULL,
    total_price NUMERIC(10, 2) NOT NULL,
    requested_delivery_time TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE order_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id),
    dish_id UUID NOT NULL REFERENCES dishes(id),
    quantity INTEGER NOT NULL,
    unit_price_snapshot NUMERIC(10, 2) NOT NULL
);