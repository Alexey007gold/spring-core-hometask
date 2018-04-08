DROP TABLE IF EXISTS public.users;
CREATE TABLE public.users
(
  id BIGSERIAL PRIMARY KEY NOT NULL,
  first_name VARCHAR(40) NOT NULL,
  last_name VARCHAR(40),
  email VARCHAR(60),
  birth_date TIMESTAMP
);
CREATE UNIQUE INDEX users_id_uindex ON public.users (id);

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
  user_id BIGINT NOT NULL,
  event_id BIGINT NOT NULL,
  time TIMESTAMP NOT NULL,
  seat BIGINT NOT NULL
);
CREATE UNIQUE INDEX tickets_id_uindex ON public.tickets (id);