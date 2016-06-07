CREATE DATABASE test
  WITH OWNER = postgres
       ENCODING = 'UTF8'
       TABLESPACE = pg_default
       LC_COLLATE = 'Chinese (Simplified)_People''s Republic of China.936'
       LC_CTYPE = 'Chinese (Simplified)_People''s Republic of China.936'
       CONNECTION LIMIT = -1;
CREATE TABLE t_user
(
  user_id bigserial NOT NULL,
  username text,
  nickname text,
  truename text,
  password text,
  birth timestamp without time zone,
  sex smallint,
  create_time timestamp without time zone,
  status smallint,
  provider_type smallint DEFAULT 0,
  provider_type1 smallint NOT NULL DEFAULT 0,
  CONSTRAINT t_user_pkey PRIMARY KEY (user_id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE t_user
  OWNER TO postgres;