CREATE TABLE vehicule (
    id SERIAL PRIMARY KEY,
    reference VARCHAR(50) NOT NULL,
    type_carburant VARCHAR(2) NOT NULL, --D: Diesel, Es: Essence, El: Electrique
    nbr_place INTEGER NOT NULL CONSTRAINT chk_nbr_place CHECK (nbr_place > 0)
);