INSERT INTO vehicule (reference, type_carburant, nbr_place) VALUES 
('v1', 'Es', 4),
('v2', 'D', 6);

INSERT INTO lieu (libelle, code) VALUES 
( 'Aeroport', 'Ae'),
( 'hotel1', 'h1'),
( 'hotel2', 'h2'),
( 'hotel3', 'h3');


INSERT INTO distance (from_lieu_id, to_lieu_id, kilometer) VALUES 
-- Distances depuis Colbert (ID 1)
(1, 2, 10),  -- Colbert -> Novotel
(1, 3, 15),  -- Colbert -> Ibis
(1, 4, 20),  -- Colbert -> Lokanga
(2, 3, 5),  -- Colbert -> Lokanga
(2, 4, 10),  -- Colbert -> Lokanga
(3, 4, 8);  -- Colbert -> Lokanga

INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CL1', 2, 2, '2026-02-17 08:00:00'),
('CL2', 3, 4, '2026-02-17 08:05:00'),
('CL3', 4, 3, '2026-02-17 08:10:00'),
('CL4', 2, 6, '2026-02-17 08:35:00'),
('CL5', 3, 4, '2026-02-17 08:40:00'),
('CL6', 4, 3, '2026-02-17 08:45:00'),
('CL7', 2, 1, '2026-02-17 09:00:00'),
('CL8', 3, 5, '2026-02-17 09:05:00'),
('CL9', 3, 4, '2026-02-17 09:30:00');
