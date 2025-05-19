--
-- PostgreSQL database dump
--

-- Dumped from database version 17.0
-- Dumped by pg_dump version 17.0

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET transaction_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: check_candidate_limit(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_candidate_limit() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    -- Подсчитываем количество кандидатов для текущего голосования
    IF (SELECT COUNT(*) FROM election_candidates WHERE election_id = NEW.election_id) >= 10 THEN
        RAISE EXCEPTION 'В голосовании не может быть более 10 кандидатов';
    ELSIF (SELECT COUNT(*) FROM election_candidates WHERE election_id = NEW.election_id) < 2 THEN
        RAISE EXCEPTION 'В голосовании должно быть хотя бы 2 кандидата';
    END IF;
    
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.check_candidate_limit() OWNER TO postgres;

--
-- Name: check_winner_status(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.check_winner_status() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    IF (SELECT is_winner FROM candidates WHERE id = NEW.candidate_id) THEN
        RAISE EXCEPTION 'Этот кандидат уже побеждал и не может участвовать снова';
    END IF;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.check_winner_status() OWNER TO postgres;

--
-- Name: create_results_table(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.create_results_table() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    table_name TEXT;
BEGIN
    table_name := 'election_results_' || NEW.id;
    EXECUTE format('
        CREATE TABLE %I (
            candidate_id INT REFERENCES candidates(id) ON DELETE CASCADE,
            candidate_name TEXT NOT NULL,
            votes_count INT DEFAULT 0
        )', table_name);
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.create_results_table() OWNER TO postgres;

--
-- Name: determine_winner(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.determine_winner() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
DECLARE
    max_votes INT;
    winner_candidate_id INT;
BEGIN
    SELECT MAX(votes_count) INTO max_votes FROM election_results WHERE election_id = NEW.election_id;
    
    -- Проверка, есть ли несколько кандидатов с одинаковым количеством голосов
    IF (SELECT COUNT(*) FROM election_results WHERE election_id = NEW.election_id AND votes_count = max_votes) > 1 THEN
        UPDATE elections SET winner_id = NULL WHERE id = NEW.election_id; -- Голосование закрывается без победителя
    ELSE
        SELECT candidate_id INTO winner_candidate_id FROM election_results WHERE election_id = NEW.election_id AND votes_count = max_votes;
        UPDATE elections SET winner_id = winner_candidate_id WHERE id = NEW.election_id;
        UPDATE candidates SET is_winner = TRUE WHERE id = winner_candidate_id; -- Отмечаем победителя
    END IF;
    
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.determine_winner() OWNER TO postgres;

--
-- Name: track_user_voting_history(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION public.track_user_voting_history() RETURNS trigger
    LANGUAGE plpgsql
    AS $$
BEGIN
    INSERT INTO user_voting_history (user_id, election_id, voted)
    SELECT id, NEW.id, FALSE FROM users;
    RETURN NEW;
END;
$$;


ALTER FUNCTION public.track_user_voting_history() OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: administrators; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.administrators (
    id integer NOT NULL,
    full_name character varying(255) NOT NULL,
    email character varying(255) NOT NULL,
    phone character varying(11) NOT NULL,
    login character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL
);


ALTER TABLE public.administrators OWNER TO postgres;

--
-- Name: administrators_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.administrators_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.administrators_id_seq OWNER TO postgres;

--
-- Name: administrators_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.administrators_id_seq OWNED BY public.administrators.id;


--
-- Name: candidates; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.candidates (
    id integer NOT NULL,
    full_name character varying(255) NOT NULL,
    birth_date character varying(15) NOT NULL,
    party character varying(255),
    biography text,
    login character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL,
    disqualified boolean DEFAULT false
);


ALTER TABLE public.candidates OWNER TO postgres;

--
-- Name: candidates_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.candidates_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.candidates_id_seq OWNER TO postgres;

--
-- Name: candidates_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.candidates_id_seq OWNED BY public.candidates.id;


--
-- Name: election_commissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.election_commissions (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    region character varying(255) NOT NULL,
    address text NOT NULL,
    contact_email character varying(255) NOT NULL,
    contact_phone character varying(11) NOT NULL,
    login character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL
);


ALTER TABLE public.election_commissions OWNER TO postgres;

--
-- Name: election_commissions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.election_commissions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.election_commissions_id_seq OWNER TO postgres;

--
-- Name: election_commissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.election_commissions_id_seq OWNED BY public.election_commissions.id;


--
-- Name: election_results; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.election_results (
    id integer NOT NULL,
    election_id integer NOT NULL,
    candidate_login character varying(100) NOT NULL,
    votes integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.election_results OWNER TO postgres;

--
-- Name: election_results_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.election_results_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.election_results_id_seq OWNER TO postgres;

--
-- Name: election_results_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.election_results_id_seq OWNED BY public.election_results.id;


--
-- Name: elections; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.elections (
    id integer NOT NULL,
    name character varying(255) NOT NULL,
    commission_id integer NOT NULL,
    start_time timestamp without time zone DEFAULT now() NOT NULL,
    end_time timestamp without time zone DEFAULT (now() + '1 day'::interval) NOT NULL
);


ALTER TABLE public.elections OWNER TO postgres;

--
-- Name: elections_22; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.elections_22 (
    id integer NOT NULL,
    login character varying(100) NOT NULL,
    votes integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.elections_22 OWNER TO username;

--
-- Name: elections_22_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

CREATE SEQUENCE public.elections_22_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.elections_22_id_seq OWNER TO username;

--
-- Name: elections_22_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: username
--

ALTER SEQUENCE public.elections_22_id_seq OWNED BY public.elections_22.id;


--
-- Name: elections_24; Type: TABLE; Schema: public; Owner: username
--

CREATE TABLE public.elections_24 (
    id integer NOT NULL,
    login character varying(100) NOT NULL,
    votes integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.elections_24 OWNER TO username;

--
-- Name: elections_24_id_seq; Type: SEQUENCE; Schema: public; Owner: username
--

CREATE SEQUENCE public.elections_24_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.elections_24_id_seq OWNER TO username;

--
-- Name: elections_24_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: username
--

ALTER SEQUENCE public.elections_24_id_seq OWNED BY public.elections_24.id;


--
-- Name: elections_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.elections_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.elections_id_seq OWNER TO postgres;

--
-- Name: elections_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.elections_id_seq OWNED BY public.elections.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
    id integer NOT NULL,
    full_name character varying(255) NOT NULL,
    birth_date character varying(10) NOT NULL,
    passport_data character varying(50) NOT NULL,
    login character varying(100) NOT NULL,
    password_hash character varying(255) NOT NULL
);


ALTER TABLE public.users OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: votes_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.votes_log (
    id integer NOT NULL,
    election_id integer NOT NULL,
    voter_login character varying(100) NOT NULL,
    candidate_login character varying(100) NOT NULL,
    vote_time timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.votes_log OWNER TO postgres;

--
-- Name: votes_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.votes_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.votes_log_id_seq OWNER TO postgres;

--
-- Name: votes_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.votes_log_id_seq OWNED BY public.votes_log.id;


--
-- Name: administrators id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrators ALTER COLUMN id SET DEFAULT nextval('public.administrators_id_seq'::regclass);


--
-- Name: candidates id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidates ALTER COLUMN id SET DEFAULT nextval('public.candidates_id_seq'::regclass);


--
-- Name: election_commissions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_commissions ALTER COLUMN id SET DEFAULT nextval('public.election_commissions_id_seq'::regclass);


--
-- Name: election_results id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_results ALTER COLUMN id SET DEFAULT nextval('public.election_results_id_seq'::regclass);


--
-- Name: elections id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.elections ALTER COLUMN id SET DEFAULT nextval('public.elections_id_seq'::regclass);


--
-- Name: elections_22 id; Type: DEFAULT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.elections_22 ALTER COLUMN id SET DEFAULT nextval('public.elections_22_id_seq'::regclass);


--
-- Name: elections_24 id; Type: DEFAULT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.elections_24 ALTER COLUMN id SET DEFAULT nextval('public.elections_24_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: votes_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes_log ALTER COLUMN id SET DEFAULT nextval('public.votes_log_id_seq'::regclass);


--
-- Data for Name: administrators; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.administrators (id, full_name, email, phone, login, password_hash) FROM stdin;
1	Иван Иванов	ivan.ivanov@example.com	89991234567	ivan_ivanov	1
\.


--
-- Data for Name: candidates; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.candidates (id, full_name, birth_date, party, biography, login, password_hash, disqualified) FROM stdin;
1	Иван Иванов	1985-06-15	Партия Прогресса	Биография кандидата Иванова	ivan_ivanov	hashed_password_1	t
4	Иван Иван Иван	2000-12-12 +03	новые люди		ivan	31821a	t
2	Мария Смирнова	1990-02-20	Партия Развития	Биография кандидата Смирновой	maria_smirnova	hashed_password_2	f
\.


--
-- Data for Name: election_commissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.election_commissions (id, name, region, address, contact_email, contact_phone, login, password_hash) FROM stdin;
2	ЦИК	Воронежская область	г.Воронеж	vor@mail.com	89509875421	voronezh	2
\.


--
-- Data for Name: election_results; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.election_results (id, election_id, candidate_login, votes) FROM stdin;
1	22	maria_smirnova	0
2	22	ivan_ivanov	1
7	24	maria_smirnova	0
8	24	ivan	2
\.


--
-- Data for Name: elections; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.elections (id, name, commission_id, start_time, end_time) FROM stdin;
22	Президентские выборы	1	2025-03-09 22:33:12.83313	2025-03-10 20:33:12
24	Губернаторские выборы	1	2025-03-12 19:10:19.035608	2025-03-13 19:10:19
\.


--
-- Data for Name: elections_22; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.elections_22 (id, login, votes) FROM stdin;
1	maria_smirnova	0
2	ivan_ivanov	1
\.


--
-- Data for Name: elections_24; Type: TABLE DATA; Schema: public; Owner: username
--

COPY public.elections_24 (id, login, votes) FROM stdin;
1	maria_smirnova	0
2	ivan	2
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, full_name, birth_date, passport_data, login, password_hash) FROM stdin;
3	Валера	13.01.2002	1234567899	valer	3
\.


--
-- Data for Name: votes_log; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.votes_log (id, election_id, voter_login, candidate_login, vote_time) FROM stdin;
6	22	valer	ivan_ivanov	2025-03-09 22:36:27.70325
7	24	valer	ivan	2025-03-12 19:10:48.433733
\.


--
-- Name: administrators_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.administrators_id_seq', 1, true);


--
-- Name: candidates_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.candidates_id_seq', 4, true);


--
-- Name: election_commissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.election_commissions_id_seq', 2, true);


--
-- Name: election_results_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.election_results_id_seq', 8, true);


--
-- Name: elections_22_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.elections_22_id_seq', 2, true);


--
-- Name: elections_24_id_seq; Type: SEQUENCE SET; Schema: public; Owner: username
--

SELECT pg_catalog.setval('public.elections_24_id_seq', 2, true);


--
-- Name: elections_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.elections_id_seq', 24, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 3, true);


--
-- Name: votes_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.votes_log_id_seq', 8, true);


--
-- Name: administrators administrators_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrators
    ADD CONSTRAINT administrators_email_key UNIQUE (email);


--
-- Name: administrators administrators_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrators
    ADD CONSTRAINT administrators_login_key UNIQUE (login);


--
-- Name: administrators administrators_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrators
    ADD CONSTRAINT administrators_phone_key UNIQUE (phone);


--
-- Name: administrators administrators_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.administrators
    ADD CONSTRAINT administrators_pkey PRIMARY KEY (id);


--
-- Name: candidates candidates_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidates
    ADD CONSTRAINT candidates_login_key UNIQUE (login);


--
-- Name: candidates candidates_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.candidates
    ADD CONSTRAINT candidates_pkey PRIMARY KEY (id);


--
-- Name: election_commissions election_commissions_contact_email_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_commissions
    ADD CONSTRAINT election_commissions_contact_email_key UNIQUE (contact_email);


--
-- Name: election_commissions election_commissions_contact_phone_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_commissions
    ADD CONSTRAINT election_commissions_contact_phone_key UNIQUE (contact_phone);


--
-- Name: election_commissions election_commissions_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_commissions
    ADD CONSTRAINT election_commissions_login_key UNIQUE (login);


--
-- Name: election_commissions election_commissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_commissions
    ADD CONSTRAINT election_commissions_pkey PRIMARY KEY (id);


--
-- Name: election_results election_results_election_id_candidate_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_results
    ADD CONSTRAINT election_results_election_id_candidate_login_key UNIQUE (election_id, candidate_login);


--
-- Name: election_results election_results_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_results
    ADD CONSTRAINT election_results_pkey PRIMARY KEY (id);


--
-- Name: elections_22 elections_22_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.elections_22
    ADD CONSTRAINT elections_22_pkey PRIMARY KEY (id);


--
-- Name: elections_24 elections_24_pkey; Type: CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.elections_24
    ADD CONSTRAINT elections_24_pkey PRIMARY KEY (id);


--
-- Name: elections elections_name_start_time_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.elections
    ADD CONSTRAINT elections_name_start_time_key UNIQUE (name, start_time);


--
-- Name: elections elections_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.elections
    ADD CONSTRAINT elections_pkey PRIMARY KEY (id);


--
-- Name: users users_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_login_key UNIQUE (login);


--
-- Name: users users_passport_data_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_passport_data_key UNIQUE (passport_data);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: votes_log votes_log_election_id_voter_login_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes_log
    ADD CONSTRAINT votes_log_election_id_voter_login_key UNIQUE (election_id, voter_login);


--
-- Name: votes_log votes_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes_log
    ADD CONSTRAINT votes_log_pkey PRIMARY KEY (id);


--
-- Name: election_results election_results_candidate_login_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_results
    ADD CONSTRAINT election_results_candidate_login_fkey FOREIGN KEY (candidate_login) REFERENCES public.candidates(login) ON DELETE CASCADE;


--
-- Name: election_results election_results_election_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.election_results
    ADD CONSTRAINT election_results_election_id_fkey FOREIGN KEY (election_id) REFERENCES public.elections(id) ON DELETE CASCADE;


--
-- Name: elections_22 elections_22_login_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.elections_22
    ADD CONSTRAINT elections_22_login_fkey FOREIGN KEY (login) REFERENCES public.candidates(login) ON DELETE CASCADE;


--
-- Name: elections_24 elections_24_login_fkey; Type: FK CONSTRAINT; Schema: public; Owner: username
--

ALTER TABLE ONLY public.elections_24
    ADD CONSTRAINT elections_24_login_fkey FOREIGN KEY (login) REFERENCES public.candidates(login) ON DELETE CASCADE;


--
-- Name: votes_log votes_log_candidate_login_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes_log
    ADD CONSTRAINT votes_log_candidate_login_fkey FOREIGN KEY (candidate_login) REFERENCES public.candidates(login) ON DELETE CASCADE;


--
-- Name: votes_log votes_log_election_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes_log
    ADD CONSTRAINT votes_log_election_id_fkey FOREIGN KEY (election_id) REFERENCES public.elections(id) ON DELETE CASCADE;


--
-- Name: votes_log votes_log_voter_login_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.votes_log
    ADD CONSTRAINT votes_log_voter_login_fkey FOREIGN KEY (voter_login) REFERENCES public.users(login) ON DELETE CASCADE;


--
-- Name: SCHEMA public; Type: ACL; Schema: -; Owner: pg_database_owner
--

GRANT ALL ON SCHEMA public TO username;


--
-- Name: TABLE administrators; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.administrators TO username;


--
-- Name: TABLE candidates; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,REFERENCES,DELETE,UPDATE ON TABLE public.candidates TO username;


--
-- Name: SEQUENCE candidates_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.candidates_id_seq TO username;


--
-- Name: TABLE election_commissions; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.election_commissions TO username;


--
-- Name: SEQUENCE election_commissions_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE public.election_commissions_id_seq TO username;


--
-- Name: TABLE election_results; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.election_results TO username;


--
-- Name: SEQUENCE election_results_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.election_results_id_seq TO username;


--
-- Name: TABLE elections; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.elections TO username;


--
-- Name: SEQUENCE elections_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE public.elections_id_seq TO username;


--
-- Name: TABLE users; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.users TO username;


--
-- Name: SEQUENCE users_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,USAGE ON SEQUENCE public.users_id_seq TO username;


--
-- Name: TABLE votes_log; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,INSERT,DELETE,UPDATE ON TABLE public.votes_log TO username;


--
-- Name: SEQUENCE votes_log_id_seq; Type: ACL; Schema: public; Owner: postgres
--

GRANT SELECT,UPDATE ON SEQUENCE public.votes_log_id_seq TO username;


--
-- PostgreSQL database dump complete
--

