CREATE TABLE dishes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    traiteur_id UUID NOT NULL REFERENCES traiteurs(id),
    name VARCHAR(255) NOT NULL,
    description TEXT,
    price NUMERIC(10, 2) NOT NULL,
    category VARCHAR(50) NOT NULL,
    photo_url VARCHAR(500),
    is_available BOOLEAN NOT NULL DEFAULT true,
    prep_time_hours INTEGER,
    version BIGINT NOT NULL DEFAULT 0,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);