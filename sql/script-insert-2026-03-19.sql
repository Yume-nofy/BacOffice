--
-- PostgreSQL database dump
--

-- Dumped from database version 14.17
-- Dumped by pg_dump version 14.17

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: assignation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.assignation (
    id integer NOT NULL,
    idtrajet integer,
    idreservation integer,
    nb_passager integer,
    ordre integer NOT NULL
);


ALTER TABLE public.assignation OWNER TO postgres;

--
-- Name: assignation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.assignation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.assignation_id_seq OWNER TO postgres;

--
-- Name: assignation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.assignation_id_seq OWNED BY public.assignation.id;


--
-- Name: distance; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.distance (
    id integer NOT NULL,
    from_lieu_id integer,
    to_lieu_id integer,
    kilometer numeric(10,2)
);


ALTER TABLE public.distance OWNER TO postgres;

--
-- Name: distance_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.distance_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.distance_id_seq OWNER TO postgres;

--
-- Name: distance_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.distance_id_seq OWNED BY public.distance.id;


--
-- Name: lieu; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.lieu (
    id integer NOT NULL,
    libelle character varying(255),
    code character varying(3)
);


ALTER TABLE public.lieu OWNER TO postgres;

--
-- Name: hotel_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hotel_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hotel_id_seq OWNER TO postgres;

--
-- Name: hotel_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.hotel_id_seq OWNED BY public.lieu.id;


--
-- Name: parametre; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.parametre (
    id integer NOT NULL,
    vitesse_moyenne numeric(5,2) DEFAULT 30.00,
    temps_attente integer DEFAULT 30
);


ALTER TABLE public.parametre OWNER TO postgres;

--
-- Name: parametre_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.parametre_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.parametre_id_seq OWNER TO postgres;

--
-- Name: parametre_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.parametre_id_seq OWNED BY public.parametre.id;


--
-- Name: reservation; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reservation (
    id integer NOT NULL,
    idclient character varying(10),
    idhotel integer,
    nb_passager integer,
    date_arrivee timestamp without time zone
);


ALTER TABLE public.reservation OWNER TO postgres;

--
-- Name: reservation_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reservation_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.reservation_id_seq OWNER TO postgres;

--
-- Name: reservation_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reservation_id_seq OWNED BY public.reservation.id;


--
-- Name: token; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.token (
    id integer NOT NULL,
    token text NOT NULL,
    date_expiration timestamp without time zone NOT NULL,
    date_creation timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.token OWNER TO postgres;

--
-- Name: token_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.token_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.token_id_seq OWNER TO postgres;

--
-- Name: token_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.token_id_seq OWNED BY public.token.id;


--
-- Name: trajet; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trajet (
    id integer NOT NULL,
    idvehicule integer,
    distance_parcourue numeric(10,2),
    date_depart timestamp without time zone NOT NULL,
    date_retour timestamp without time zone NOT NULL
);


ALTER TABLE public.trajet OWNER TO postgres;

--
-- Name: trajet_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trajet_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.trajet_id_seq OWNER TO postgres;

--
-- Name: trajet_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trajet_id_seq OWNED BY public.trajet.id;


--
-- Name: vehicule; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vehicule (
    id integer NOT NULL,
    reference character varying(50) NOT NULL,
    type_carburant character varying(2) NOT NULL,
    nbr_place integer NOT NULL,
    CONSTRAINT chk_nbr_place CHECK ((nbr_place > 0))
);


ALTER TABLE public.vehicule OWNER TO postgres;

--
-- Name: vehicule_disponibilite; Type: VIEW; Schema: public; Owner: postgres
--

CREATE VIEW public.vehicule_disponibilite AS
 SELECT v.id,
    v.reference,
    v.type_carburant,
    v.nbr_place,
    COALESCE(( SELECT max(t.date_retour) AS max
           FROM public.trajet t
          WHERE (t.idvehicule = v.id)), '1970-01-01 00:00:00'::timestamp without time zone) AS derniere_date_retour,
    COALESCE(( SELECT count(*) AS count
           FROM public.trajet t
          WHERE (t.idvehicule = v.id)), (0)::bigint) AS nombre_trajets
   FROM public.vehicule v
  ORDER BY v.id;


ALTER TABLE public.vehicule_disponibilite OWNER TO postgres;

--
-- Name: vehicule_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vehicule_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.vehicule_id_seq OWNER TO postgres;

--
-- Name: vehicule_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.vehicule_id_seq OWNED BY public.vehicule.id;


--
-- Name: assignation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignation ALTER COLUMN id SET DEFAULT nextval('public.assignation_id_seq'::regclass);


--
-- Name: distance id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distance ALTER COLUMN id SET DEFAULT nextval('public.distance_id_seq'::regclass);


--
-- Name: lieu id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lieu ALTER COLUMN id SET DEFAULT nextval('public.hotel_id_seq'::regclass);


--
-- Name: parametre id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parametre ALTER COLUMN id SET DEFAULT nextval('public.parametre_id_seq'::regclass);


--
-- Name: reservation id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation ALTER COLUMN id SET DEFAULT nextval('public.reservation_id_seq'::regclass);


--
-- Name: token id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token ALTER COLUMN id SET DEFAULT nextval('public.token_id_seq'::regclass);


--
-- Name: trajet id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet ALTER COLUMN id SET DEFAULT nextval('public.trajet_id_seq'::regclass);


--
-- Name: vehicule id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicule ALTER COLUMN id SET DEFAULT nextval('public.vehicule_id_seq'::regclass);


--
-- Data for Name: assignation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.assignation (id, idtrajet, idreservation, nb_passager, ordre) FROM stdin;
\.


--
-- Data for Name: distance; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.distance (id, from_lieu_id, to_lieu_id, kilometer) FROM stdin;
1	1	2	10.00
2	1	3	15.00
3	1	4	20.00
4	2	3	5.00
5	2	4	10.00
6	3	4	8.00
\.


--
-- Data for Name: lieu; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.lieu (id, libelle, code) FROM stdin;
1	Aeroport	Ae
2	hotel1	h1
3	hotel2	h2
4	hotel3	h3
\.


--
-- Data for Name: parametre; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.parametre (id, vitesse_moyenne, temps_attente) FROM stdin;
3	60.00	30
\.


--
-- Data for Name: reservation; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reservation (id, idclient, idhotel, nb_passager, date_arrivee) FROM stdin;
1	CL1	2	2	2026-02-17 08:00:00
2	CL2	3	4	2026-02-17 08:05:00
3	CL3	4	3	2026-02-17 08:10:00
4	CL4	2	6	2026-02-17 08:35:00
5	CL5	3	4	2026-02-17 08:40:00
6	CL6	4	3	2026-02-17 08:45:00
7	CL7	2	1	2026-02-17 09:00:00
8	CL8	3	5	2026-02-17 09:05:00
9	CL9	3	4	2026-02-17 09:30:00
\.


--
-- Data for Name: token; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.token (id, token, date_expiration, date_creation) FROM stdin;
1	2NIdndTodiZree	2026-02-24 11:31:45.989151	2026-02-24 10:31:46.199309
2	SW2PwDtGxU6jhT	2026-02-25 10:31:46.248373	2026-02-24 10:31:46.33656
3	pwnYhruJnIZylS	2026-03-24 10:31:46.349789	2026-02-24 10:31:46.393327
4	S19LE7Vs3d9cIN	2026-02-23 10:31:46.410882	2026-02-24 10:31:46.455343
5	tqZjErpI83PbQg	2026-02-27 10:59:05.951511	2026-02-27 09:59:07.855241
6	gg6D9dvwkONYs4	2026-02-28 09:59:07.983033	2026-02-27 09:59:08.043442
7	PCCa2HfPHvFLif	2026-03-27 09:59:08.065686	2026-02-27 09:59:08.136403
8	BVo0brwR7l95m9	2026-02-26 09:59:08.150275	2026-02-27 09:59:08.205484
\.


--
-- Data for Name: trajet; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trajet (id, idvehicule, distance_parcourue, date_depart, date_retour) FROM stdin;
\.


--
-- Data for Name: vehicule; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vehicule (id, reference, type_carburant, nbr_place) FROM stdin;
1	v1	Es	6
2	v2	D	4
\.


--
-- Name: assignation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.assignation_id_seq', 8, true);


--
-- Name: distance_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.distance_id_seq', 6, true);


--
-- Name: hotel_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hotel_id_seq', 4, true);


--
-- Name: parametre_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.parametre_id_seq', 3, true);


--
-- Name: reservation_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reservation_id_seq', 9, true);


--
-- Name: token_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.token_id_seq', 8, true);


--
-- Name: trajet_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trajet_id_seq', 6, true);


--
-- Name: vehicule_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vehicule_id_seq', 2, true);


--
-- Name: assignation assignation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignation
    ADD CONSTRAINT assignation_pkey PRIMARY KEY (id);


--
-- Name: distance distance_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distance
    ADD CONSTRAINT distance_pkey PRIMARY KEY (id);


--
-- Name: lieu hotel_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.lieu
    ADD CONSTRAINT hotel_pkey PRIMARY KEY (id);


--
-- Name: parametre parametre_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.parametre
    ADD CONSTRAINT parametre_pkey PRIMARY KEY (id);


--
-- Name: reservation reservation_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT reservation_pkey PRIMARY KEY (id);


--
-- Name: token token_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.token
    ADD CONSTRAINT token_pkey PRIMARY KEY (id);


--
-- Name: trajet trajet_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet
    ADD CONSTRAINT trajet_pkey PRIMARY KEY (id);


--
-- Name: vehicule vehicule_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicule
    ADD CONSTRAINT vehicule_pkey PRIMARY KEY (id);


--
-- Name: idx_token_value; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX idx_token_value ON public.token USING btree (token);


--
-- Name: assignation assignation_idreservation_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignation
    ADD CONSTRAINT assignation_idreservation_fkey FOREIGN KEY (idreservation) REFERENCES public.reservation(id);


--
-- Name: assignation assignation_idtrajet_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.assignation
    ADD CONSTRAINT assignation_idtrajet_fkey FOREIGN KEY (idtrajet) REFERENCES public.trajet(id);


--
-- Name: distance distance_from_lieu_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distance
    ADD CONSTRAINT distance_from_lieu_id_fkey FOREIGN KEY (from_lieu_id) REFERENCES public.lieu(id);


--
-- Name: distance distance_to_lieu_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.distance
    ADD CONSTRAINT distance_to_lieu_id_fkey FOREIGN KEY (to_lieu_id) REFERENCES public.lieu(id);


--
-- Name: reservation fk_hotel; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reservation
    ADD CONSTRAINT fk_hotel FOREIGN KEY (idhotel) REFERENCES public.lieu(id);


--
-- Name: trajet trajet_idvehicule_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trajet
    ADD CONSTRAINT trajet_idvehicule_fkey FOREIGN KEY (idvehicule) REFERENCES public.vehicule(id);


--
-- PostgreSQL database dump complete
--

