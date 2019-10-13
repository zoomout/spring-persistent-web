SET search_path = public;

-- TABLE
CREATE TABLE customers (
  id         BIGSERIAL PRIMARY KEY ,
  name       VARCHAR(255) NOT NULL
);