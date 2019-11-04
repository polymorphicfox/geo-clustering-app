--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.13
-- Dumped by pg_dump version 9.6.13

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

SET default_with_oids = false;

CREATE SCHEMA geo_objects_db;

--
-- Name: geo_objects; Type: TABLE; Schema: geo_objects_db; Owner: postgres
--

CREATE TABLE geo_objects_db.geo_objects (
    id character varying(255) NOT NULL,
    address text,
    city text,
    latitude double precision,
    longitude double precision,
    name text,
    quadkey character varying(23)
);


ALTER TABLE geo_objects_db.geo_objects OWNER TO postgres;

--
-- Data for Name: geo_objects; Type: TABLE DATA; Schema: geo_objects_db; Owner: postgres
--

INSERT INTO geo_objects_db.geo_objects (id, address, city, latitude, longitude, name, quadkey)
VALUES
   ('84aaa57f-8846-48e8-80d9-1337909bb620','Шексна, пер. Путейский, д. 4','Вологодская область',59.217852999999998,38.511791000000002,'Шексна, пер. Путейский', '12013033033200121103301');


--
-- PostgreSQL database dump complete
--

