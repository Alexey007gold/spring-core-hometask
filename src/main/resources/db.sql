DROP TABLE IF EXISTS public.users;
CREATE TABLE public.users
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40),
  email VARCHAR(60),
  login VARCHAR(60) NOT NULL UNIQUE,
  password VARCHAR(5000) NOT NULL,
  birth_date TIMESTAMP
);
DROP TABLE IF EXISTS public.user_roles;
CREATE TABLE public.user_roles
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  user_id BIGINT NOT NULL,
  role VARCHAR(40) NOT NULL
);
CREATE UNIQUE INDEX user_roles_id_uindex ON public.user_roles (id);
DROP TABLE IF EXISTS public.user_accounts;
CREATE TABLE public.user_accounts
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  user_id BIGINT NOT NULL,
  balance DOUBLE PRECISION NOT NULL
);
CREATE UNIQUE INDEX user_accounts_id_uindex ON public.user_accounts (id);

DROP TABLE IF EXISTS public.events;
CREATE TABLE public.events
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  name VARCHAR(50) NOT NULL,
  rating INT NOT NULL,
  base_price DOUBLE PRECISION NOT NULL
);
CREATE UNIQUE INDEX events_id_uindex ON public.events (id);

DROP TABLE IF EXISTS public.event_dates;
CREATE TABLE public.event_dates
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  event_id BIGINT NOT NULL,
  time TIMESTAMP NOT NULL,
  auditorium_name VARCHAR(50) NOT NULL
);
CREATE UNIQUE INDEX event_dates_id_uindex ON public.event_dates (id);

DROP TABLE IF EXISTS public.tickets;
CREATE TABLE public.tickets
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  user_id BIGINT,
  event_id BIGINT NOT NULL,
  time TIMESTAMP NOT NULL,
  seat INT NOT NULL,
  price DOUBLE PRECISION NOT NULL
);
CREATE UNIQUE INDEX tickets_id_uindex ON public.tickets (id);

DROP TABLE IF EXISTS public.event_stats;
CREATE TABLE public.event_stats
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  event_id BIGINT NOT NULL,
  access_by_name INT NOT NULL,
  price_query INT NOT NULL,
  tickets_booked INT NOT NULL
);
CREATE UNIQUE INDEX event_stats_id_uindex ON public.event_stats (id);

DROP TABLE IF EXISTS public.discount_stats;
CREATE TABLE public.discount_stats
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  discount_type VARCHAR(60) NOT NULL,
  user_id BIGINT,
  times INT NOT NULL
);
CREATE UNIQUE INDEX discount_stats_id_uindex ON public.discount_stats (id);

DROP TABLE IF EXISTS persistent_logins;
CREATE TABLE persistent_logins (
  username varchar(64) not null,
  series varchar(64) not null,
  token varchar(64) not null,
  last_used timestamp not null,
  PRIMARY KEY (series)
);