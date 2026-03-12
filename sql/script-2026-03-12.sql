INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CLT-011', 3, 13, '2025-02-17 08:00:00');
INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CLT-022', 2, 6, '2025-02-17 08:00:00');
INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CLT-032', 3, 3, '2025-02-17 08:00:00');
INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES ('CLT-042', 4, 1, '2025-02-17 08:00:00');
INSERT INTO vehicule (reference, type_carburant, nbr_place) VALUES 
('PEUGEOT-2', 'D', 18),
('TESLA-02', 'El', 10);




INSERT INTO vehicule (reference, type_carburant, nbr_place) VALUES 
('vehicule1', 'D', 12),
('vehicule2', 'Es', 5),
('vehicule3', 'D', 5),
('vehicule4', 'Es', 12);

INSERT INTO lieu (libelle, code) VALUES 
( 'Aeroport', 'Ae'),
( 'Hotel1', 'H1');

INSERT INTO distance (from_lieu_id, to_lieu_id, kilometer) VALUES 
-- Distances depuis Colbert (ID 1)
(1, 2, 50);  -- Colbert -> Novotel

INSERT INTO reservation(idclient,idhotel,nb_passager,date_arrivee) VALUES 
('Client1', 2, 7, '2026-03-12 09:00:00'),
('Client2', 2, 11, '2026-03-12 09:00:00'),
('Client3', 2, 3, '2026-03-12 09:00:00'),
('Client4', 2, 1, '2026-03-12 09:00:00'),
('Client5', 2, 2, '2026-03-12 09:00:00'),
('Client6', 2, 20, '2026-03-12 09:00:00');

INSERT INTO parametre ( vitesse_moyenne, temps_attente) 
VALUES (50, 30);