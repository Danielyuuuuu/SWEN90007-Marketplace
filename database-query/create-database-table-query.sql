CREATE TABLE "account" (
    id VARCHAR(36) PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    firstname VARCHAR(50) NOT NULL,
    lastname VARCHAR(50) NOT NULL,
    password VARCHAR(128) NOT NULL,
    shipping_address VARCHAR(150) NULL,
    role VARCHAR(10) NOT NULL,
    seller_group VARCHAR(36) NULL,
    CONSTRAINT check_role CHECK (role IN('USER', 'ADMIN', 'SELLER')),
    CONSTRAINT email_unique UNIQUE (email)
);

CREATE TABLE "seller_group" (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE "purchase" (
    id VARCHAR(36) PRIMARY KEY,
    title VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    price FLOAT(2) NOT NULL,
    buyer VARCHAR(36) NOT NULL,
    purchase_status VARCHAR(50) NOT NULL,
    product VARCHAR(36) NOT NULL,
    seller_group VARCHAR(36) NOT NULL,
    fixed_price_listing VARCHAR(36) NULL,
    auction_listing VARCHAR(36) NULL,
    CONSTRAINT check_purchase_status CHECK (purchase_status IN('placed', 'processed', 'fulfilled', 'cancelled'))
);

CREATE TABLE "fixed_price_listing" (
    id VARCHAR(36) PRIMARY KEY,
    listing_title VARCHAR(50) NOT NULL,
    quantity INTEGER NOT NULL,
    price FLOAT(2) NOT NULL,
    description VARCHAR(200) NOT NULL,
    seller_group VARCHAR(36) NOT NULL,
    product VARCHAR(36) NOT NULL,
    archive BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE "product" (
    id VARCHAR(36) PRIMARY KEY,
    category VARCHAR(36) NOT NULL,
    brand VARCHAR(50) NOT NULL,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE "category" (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE "auction_listing" (
    id VARCHAR(36) PRIMARY KEY,
    listing_title VARCHAR(50) NOT NULL,
    quantity INTEGER DEFAULT 1 NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    price FLOAT(2) NOT NULL,
    description VARCHAR(50) NOT NULL,
    seller_group VARCHAR(36) NOT NULL,
    product VARCHAR(36) NOT NULL,
    archive BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE "bid" (
    id VARCHAR(36) PRIMARY KEY,
    bid_price FLOAT(2) NOT NULL,
    buyer VARCHAR(36) NOT NULL,
    auction_listing VARCHAR(36) NOT NULL
);

ALTER TABLE "account" ADD CONSTRAINT user_seller_group_foreign FOREIGN KEY (seller_group) REFERENCES "seller_group" (id);
ALTER TABLE "purchase" ADD CONSTRAINT purchase_buyer_foreign FOREIGN KEY (buyer) REFERENCES "account" (id);
ALTER TABLE "purchase" ADD CONSTRAINT purchase_product_foreign FOREIGN KEY (product) REFERENCES "product" (id);
ALTER TABLE "purchase" ADD CONSTRAINT purchase_seller_group_foreign FOREIGN KEY (seller_group) REFERENCES "seller_group" (id);
ALTER TABLE "purchase" ADD CONSTRAINT purchase_fixed_price_listing_foreign FOREIGN KEY (fixed_price_listing) REFERENCES "fixed_price_listing" (id);
ALTER TABLE "purchase" ADD CONSTRAINT purchase_auction_listing_foreign FOREIGN KEY (auction_listing) REFERENCES "auction_listing" (id);
ALTER TABLE "fixed_price_listing" ADD CONSTRAINT fixed_price_listing_seller_group_foreign FOREIGN KEY (seller_group) REFERENCES "seller_group" (id);
ALTER TABLE "fixed_price_listing" ADD CONSTRAINT fixed_price_listing_product_foreign FOREIGN KEY (product) REFERENCES "product" (id);
ALTER TABLE "product" ADD CONSTRAINT product_category_foreign FOREIGN KEY (category) REFERENCES "category" (id);
ALTER TABLE "auction_listing" ADD CONSTRAINT auction_listing_seller_group_foreign FOREIGN KEY (seller_group) REFERENCES "seller_group" (id);
ALTER TABLE "auction_listing" ADD CONSTRAINT auction_listing_product_foreign FOREIGN KEY (product) REFERENCES "product" (id);
ALTER TABLE "bid" ADD CONSTRAINT bid_buyer_foreign FOREIGN KEY (buyer) REFERENCES "account" (id);
ALTER TABLE "bid" ADD CONSTRAINT bid_auction_listing_foreign FOREIGN KEY (auction_listing) REFERENCES "auction_listing" (id);