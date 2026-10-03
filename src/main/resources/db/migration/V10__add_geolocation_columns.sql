ALTER TABLE traiteurs ADD COLUMN latitude NUMERIC(9, 6);
ALTER TABLE traiteurs ADD COLUMN longitude NUMERIC(9, 6);

ALTER TABLE orders ADD COLUMN delivery_latitude NUMERIC(9, 6);
ALTER TABLE orders ADD COLUMN delivery_longitude NUMERIC(9, 6);