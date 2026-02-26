TRUNCATE TABLE reservations RESTART IDENTITY CASCADE;

TRUNCATE TABLE lieu RESTART IDENTITY CASCADE;

INSERT INTO parametre ( vitesse_moyenne, temps_attente) 
VALUES (30, 30);

INSERT INTO lieu (libelle, code) VALUES 
( 'Colbert', 'COL'),
( 'Novotel', 'NOV'),
( 'Ibis', 'IBI'),
( 'Lokanga', 'LOK');

INSERT INTO distance (from_lieu_id, to_lieu_id, kilometer) VALUES 
-- Distances depuis Colbert (ID 1)
(1, 2, 2.5),  -- Colbert -> Novotel
(1, 3, 1.8),  -- Colbert -> Ibis
(1, 4, 0.5),  -- Colbert -> Lokanga
-- Distances depuis Novotel (ID 2)
(2, 3, 3.2),  -- Novotel -> Ibis
(2, 4, 2.8),  -- Novotel -> Lokanga
-- Distances depuis Ibis (ID 3)
(3, 4, 2.1);  -- Ibis -> Lokanga
