ALTER TABLE hotel RENAME TO lieu;

ALTER TABLE lieu ADD COLUMN code VARCHAR(3);

ALTER TABLE lieu RENAME COLUMN nom TO libelle;

CREATE TABLE parametre (
    id SERIAL PRIMARY KEY,
    vitesse_moyenne NUMERIC(5,2) DEFAULT 30.00,
    temps_attente INTEGER DEFAULT 30
);

CREATE TABLE distance (
    id SERIAL PRIMARY KEY,
    from_lieu_id INTEGER REFERENCES lieu(id),
    to_lieu_id INTEGER REFERENCES lieu(id),
    kilometer NUMERIC(10,2)
);
