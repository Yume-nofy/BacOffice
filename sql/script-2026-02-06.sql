    -- Supprimer la base si elle existe
    DROP DATABASE IF EXISTS tourOperateur;

    -- Creer la base
    CREATE DATABASE tourOperateur;

    -- Se connecter à la base (dans psql)
    \c tourOperateur;

    -- Table des hôtels
    CREATE TABLE hotel (
        id SERIAL PRIMARY KEY,
        nom VARCHAR(255)
    );

    -- Table des reservations
    CREATE TABLE reservation (
        id SERIAL PRIMARY KEY,
        idclient VARCHAR(10),
        idhotel INT,
        nb_passager INT,
        date_arrivee TIMESTAMP
    );
    ALTER TABLE reservation
    ADD CONSTRAINT fk_hotel FOREIGN KEY (idhotel) REFERENCES hotel(id);

