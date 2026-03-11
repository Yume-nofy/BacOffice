TRUNCATE TABLE reservations RESTART IDENTITY CASCADE;

TRUNCATE TABLE lieu RESTART IDENTITY CASCADE;

INSERT INTO parametre ( vitesse_moyenne, temps_attente) 
VALUES (30, 30);

INSERT INTO lieu (libelle, code) VALUES 
( 'Colbert', 'COL'),
( 'Novotel', 'NOV'),
( 'Ibis', 'IBI'),
( 'Lokanga', 'LOK');

INSERT INTO lieu (libelle, code) VALUES 
( 'HotelFive', 'HFE');


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

INSERT INTO distance (from_lieu_id, to_lieu_id, kilometer) VALUES 
-- Connexions depuis/vers HotelFive
(5, 1, 1.2),  -- HotelFive -> Colbert
(5, 2, 3.5),  -- HotelFive -> Novotel
(5, 3, 0.8),  -- HotelFive -> Ibis
(5, 4, 2.3);  -- HotelFive -> Lokanga
INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CLT-001', 3, 3, '2026-02-17 08:00:00');
INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CLT-002', 2, 2, '2026-02-17 08:00:00');