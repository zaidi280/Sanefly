CREATE TABLE delivery_companies (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    company_name VARCHAR(255) NOT NULL,
    verified_by_admin BOOLEAN NOT NULL DEFAULT false,
    is_active BOOLEAN NOT NULL DEFAULT true,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE livreurs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    delivery_company_id UUID NOT NULL REFERENCES delivery_companies(id),
    is_available BOOLEAN NOT NULL DEFAULT true,
    current_latitude NUMERIC(9, 6),
    current_longitude NUMERIC(9, 6),
    last_location_update TIMESTAMP,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

CREATE TABLE delivery_offers (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id UUID NOT NULL REFERENCES orders(id),
    livreur_id UUID NOT NULL REFERENCES livreurs(id),
    status VARCHAR(50) NOT NULL,
    offered_at TIMESTAMP NOT NULL,
    responded_at TIMESTAMP
);