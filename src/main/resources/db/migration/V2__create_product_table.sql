-- TABLE
CREATE TABLE product (
  id          BIGSERIAL PRIMARY KEY,
  title       VARCHAR(255) NOT NULL
);

CREATE TABLE customer_product (
  customer_id    BIGSERIAL NOT NULL,
  product_id     BIGSERIAL NOT NULL,
  PRIMARY KEY (customer_id, product_id),
  FOREIGN KEY (customer_id) REFERENCES customer(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);