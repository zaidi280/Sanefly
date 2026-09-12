CREATE TABLE traiteurs (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id UUID NOT NULL UNIQUE REFERENCES users(id),
    business_name VARCHAR(255) NOT NULL,
    description TEXT,
    verified_by_admin BOOLEAN NOT NULL DEFAULT false,
    rating_avg DOUBLE PRECISION,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);