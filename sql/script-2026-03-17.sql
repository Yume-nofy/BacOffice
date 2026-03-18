CREATE TABLE trajet (
    id SERIAL PRIMARY KEY,
    idvehicule INTEGER REFERENCES vehicule(id),
    distance_parcourue NUMERIC(10,2),
    date_depart TIMESTAMP NOT NULL,
    date_retour TIMESTAMP NOT NULL
);

CREATE TABLE assignation (
    id SERIAL PRIMARY KEY,
    idtrajet INTEGER REFERENCES trajet(id),
    idreservation INTEGER REFERENCES reservation(id),
    nb_passager INTEGER,
    ordre INTEGER NOT NULL 
);

CREATE OR REPLACE VIEW vehicule_disponibilite AS
SELECT 
    v.id,
    v.reference,
    v.type_carburant,
    v.nbr_place,
    COALESCE(
        (SELECT MAX(t.date_retour) 
         FROM trajet t 
         WHERE t.idvehicule = v.id), 
        TIMESTAMP '1970-01-01 00:00:00'
    ) AS derniere_date_retour,
    COALESCE(
        (SELECT COUNT(*) 
         FROM trajet t 
         WHERE t.idvehicule = v.id), 
        0
    ) AS nombre_trajets
FROM vehicule v
ORDER BY v.id;