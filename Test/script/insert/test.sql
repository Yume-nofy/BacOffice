INSERT INTO hotel (nom, code) VALUES
('Aeroport', 'IVAT'),
('Hotel 1','H1'),
('Hotel 2', 'H2');

INSERT INTO type_carburant (libelle, code) VALUES
('Essence', 'ES'),
('Diesel', 'D');

INSERT INTO vehicule (reference, capacite, id_type_carburant) VALUES
('V1', 5, 2),
('V2', 5, 1),
('V3', 12, 2),
('V4', 9, 2),
('V5', 12, 1);


INSERT INTO parametre (cle, valeur, type) VALUES
('temps_attente', '00:30', 'TIME'),
('vitesse_moyenne', '50', 'INT');

INSERT INTO reservation (id_client, nombre_passager, date_arrivee, id_hotel) VALUES
('0001', 7, '2026-03-25 09:00:00', 2), -- R1
('0002', 20, '2026-03-25 08:00:00', 3), -- R2
('0003', 3, '2026-03-25 09:10:00', 2), -- R3
('0004', 10, '2026-03-25 09:15:00', 2), -- R4
('0005', 5, '2026-03-25 09:20:00', 2), -- R5
('0006', 12, '2026-03-25 13:30:00', 2); -- R6

INSERT INTO distance (kilometre, id_from_hotel, id_to_hotel) VALUES
(90, 1, 2),
(35, 1, 3),
(60, 2, 3);

UPDATE vehicule SET heure_disponible='09:00' WHERE id = 1;
UPDATE vehicule SET heure_disponible='09:00' WHERE id = 2;
UPDATE vehicule SET heure_disponible='08:00' WHERE id = 3;
UPDATE vehicule SET heure_disponible='09:00' WHERE id = 4;
UPDATE vehicule SET heure_disponible='13:00' WHERE id = 5;


TRUNCATE TABLE 
    distance,
    reservation,
    vehicule,
    type_carburant,
    hotel,
    parametre
RESTART IDENTITY CASCADE;