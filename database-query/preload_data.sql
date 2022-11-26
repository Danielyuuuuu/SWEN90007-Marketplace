INSERT INTO category (id, name) VALUES ('235afe6a-7e98-4689-acf0-ce16db3ad625', 'Electronics');
INSERT INTO category (id, name) VALUES ('f83ea05f-0dba-4239-aab8-e0259dc87894', 'Clothing Shoes & Accessories');
INSERT INTO category (id, name) VALUES ('cccc4a2d-2297-4c21-b72c-256ccf5afbe5', 'Toys');
INSERT INTO category (id, name) VALUES ('f16e091e-6733-4b46-aa84-9ca6ca41279a', 'Sports');
INSERT INTO category (id, name) VALUES ('6db8c13e-bc29-4d30-9edf-b4a6abd39202', 'Home & Garden');
INSERT INTO category (id, name) VALUES ('8f6c135e-fd52-4bda-b492-cf2b2cef9f21', 'Health & Beauty');

INSERT INTO product (id, category, brand, name) VALUES ('7fbd1bdb-5573-4975-a700-9d6ebadb5aa0', '235afe6a-7e98-4689-acf0-ce16db3ad625', 'Apple', 'iPhone 14 Pro Max 256GB');
INSERT INTO product (id, category, brand, name) VALUES ('99557742-7c5f-49d8-bb14-f628224dcf70', '235afe6a-7e98-4689-acf0-ce16db3ad625', 'Samsung', 'Galaxy S22 Ultra 256GB');
INSERT INTO product (id, category, brand, name) VALUES ('34e9f524-b775-4776-9dd5-0a91961b8143', 'f83ea05f-0dba-4239-aab8-e0259dc87894', 'Nike', 'Nike Air Force 1');
INSERT INTO product (id, category, brand, name) VALUES ('9bdaa320-8397-421a-995e-ad1d6b97463b', 'f83ea05f-0dba-4239-aab8-e0259dc87894', 'Adidas', 'Yeezey Boost 350');
INSERT INTO product (id, category, brand, name) VALUES ('19c6136e-3fd2-402e-8c9e-36bb814a8925', 'cccc4a2d-2297-4c21-b72c-256ccf5afbe5', 'Nerf', 'Nerf Elite 2.0');
INSERT INTO product (id, category, brand, name) VALUES ('036917a3-716a-4ada-9af3-46fb442406a9', 'cccc4a2d-2297-4c21-b72c-256ccf5afbe5', 'Lego', 'Batman Batmobile Tumbler');
INSERT INTO product (id, category, brand, name) VALUES ('7db1d8ac-d5a5-45c3-85c6-a2ed7c4fde46', 'f16e091e-6733-4b46-aa84-9ca6ca41279a', 'Yonex', 'Yonex Astrox 99 PRO');
INSERT INTO product (id, category, brand, name) VALUES ('cd959481-b529-4492-9dbc-ebb8ec609650', 'f16e091e-6733-4b46-aa84-9ca6ca41279a', 'Gilbert', 'Gilbert Vector TR Rugby Ball');
INSERT INTO product (id, category, brand, name) VALUES ('a1c180c6-5195-4dbe-9dfa-b965341041ce', '6db8c13e-bc29-4d30-9edf-b4a6abd39202', 'IKEA', 'KLEPPSTAD Wardrobe');
INSERT INTO product (id, category, brand, name) VALUES ('03f31bf8-87ad-4c7b-a9a4-253b592a47e5', '6db8c13e-bc29-4d30-9edf-b4a6abd39202', 'Bosch', 'Cordless Lawnmower Brushless');
INSERT INTO product (id, category, brand, name) VALUES ('91f8b3e9-547a-480d-92f2-bbbf34fb38b6', '8f6c135e-fd52-4bda-b492-cf2b2cef9f21', 'Sephora', 'Gloss Bomb Cream');
INSERT INTO product (id, category, brand, name) VALUES ('2e4c2e5b-ca95-48d1-b343-a56c83ce7ab4', '8f6c135e-fd52-4bda-b492-cf2b2cef9f21', 'Dior', 'Sauvage Eau de Parfum');

INSERT INTO seller_group (id, name) VALUES ('593aab5d-96f6-459a-82ee-d5c83bc55309', 'MS-Quokka');

-- Password: lB5f0dXj
INSERT INTO account (id, email, firstname, lastname, password, shipping_address, role) VALUES ('9c8dcad7-36ba-4ac6-b6fa-630e1883f4c9', 'admin@gmail.com', 'Janice', 'Miranda', 'c61394ea0224bfd95f7b321065f58abe0993f6c5dda7d874d9b82f53d7daa675c6cbebb5c79dabe8', '43 Cecil Street, Macquarie Centre, New South Wales, 2113', 'ADMIN');

-- Password: MPstbMX7
INSERT INTO account (id, email, firstname, lastname, password, shipping_address, role) VALUES ('4b0a488e-93c1-421e-8823-1459ac7acac8', 'buyer@gmail.com', 'Henry', 'Nolan', '02e6ebd9f205428bb1635e69593d50dac73df267b748c678cf488d0adfae92486e49da6ed5166231', '32 Mildura Street, Mathinna, Tasmania, 7214', 'USER');

-- Password: HenGiM2x
INSERT INTO account (id, email, firstname, lastname, password, shipping_address, role) VALUES ('a56220e6-cea4-4ac1-9b1f-3dd586f822b0', 'buyer2@gmail.com', 'Marshall', 'Berry', '07570108d8611fe7540bdba489986f31711280d3777e0415780cf21e5ec9816bcdac8c2a132ee8cb', '44 Daly Terrace, Bedford, Western Australia, 6052', 'USER');

-- Password: W7db9oM7
INSERT INTO account (id, email, firstname, lastname, password, shipping_address, role, seller_group) VALUES ('5dcd2440-877f-47f9-b9a1-b708a5bd9bc2', 'seller@gmail.com', 'Sophie', 'Calvert', '2edd8268a4ea4600a6fd26a1e135d60fb0b9539586fd8999c8f534c77a321aef1c563eb7e947f437', '48 Sunraysia Road, Barkstead, Victoria, 3352', 'SELLER', '593aab5d-96f6-459a-82ee-d5c83bc55309');

-- Password: Low9GZ0Q
INSERT INTO account (id, email, firstname, lastname, password, shipping_address, role, seller_group) VALUES ('44f9b8ee-db9a-4eef-921d-284064d5d2ef', 'seller2@gmail.com', 'Precious', 'Bate', '20bf30af1d81988126baf43573ae40e41d37530229ac2f46e2680c2380c291e6b309ce7a225b878c', '94 Wynyard Street, Talbingo, New South Wales, 2720', 'SELLER', '593aab5d-96f6-459a-82ee-d5c83bc55309');

INSERT INTO fixed_price_listing (id, listing_title, quantity, price, description, seller_group, product) VALUES ('0e923f33-13cd-4189-afa8-db65f386fef9', 'iPhone 14 Pro Max 256GB', 5, 2099, 'Brand new iPhone 14 Pro Max 256GB', '593aab5d-96f6-459a-82ee-d5c83bc55309', '7fbd1bdb-5573-4975-a700-9d6ebadb5aa0');
INSERT INTO fixed_price_listing (id, listing_title, quantity, price, description, seller_group, product) VALUES ('16f01056-7382-4c79-9e02-a0ea91402ed8', 'Gloss Bomb Cream', 7, 29, 'Brand new Gloss Bomb Cream', '593aab5d-96f6-459a-82ee-d5c83bc55309', '91f8b3e9-547a-480d-92f2-bbbf34fb38b6');
INSERT INTO fixed_price_listing (id, listing_title, quantity, price, description, seller_group, product) VALUES ('cc5a009e-9e05-49f3-af6e-90cfc0f8ee09', 'Batman Batmobile Tumbler', 3, 159, 'Brand new Batman Batmobile Tumbler', '593aab5d-96f6-459a-82ee-d5c83bc55309', '036917a3-716a-4ada-9af3-46fb442406a9');

INSERT INTO purchase (id, title, quantity, price, buyer, purchase_status, product, seller_group, fixed_price_listing, auction_listing) VALUES ('d60db374-3d77-46f3-8188-ae3aef144a05', 'iPhone 14 Pro Max 256GB', 1, 2099, '4b0a488e-93c1-421e-8823-1459ac7acac8', 'placed', '7fbd1bdb-5573-4975-a700-9d6ebadb5aa0', '593aab5d-96f6-459a-82ee-d5c83bc55309', '0e923f33-13cd-4189-afa8-db65f386fef9', null);
INSERT INTO purchase (id, title, quantity, price, buyer, purchase_status, product, seller_group, fixed_price_listing, auction_listing) VALUES ('965b0df7-6a53-437e-bb2a-fae045f23f88', 'Batman Batmobile Tumbler', 2, 318, '4b0a488e-93c1-421e-8823-1459ac7acac8', 'placed', '036917a3-716a-4ada-9af3-46fb442406a9', '593aab5d-96f6-459a-82ee-d5c83bc55309', 'cc5a009e-9e05-49f3-af6e-90cfc0f8ee09', null);

INSERT INTO auction_listing (id, listing_title, quantity, start_time, end_time, price, description, seller_group, product) VALUES ('d7ddc029-8f72-473e-83df-e97485bc360b', 'Nerf Elite 2.0', 1, now() - INTERVAL '2 hour', now() + INTERVAL '100 hour', 25, 'Brand new Nerf Elite 2.0', '593aab5d-96f6-459a-82ee-d5c83bc55309', '19c6136e-3fd2-402e-8c9e-36bb814a8925');

INSERT INTO bid (id, bid_price, buyer, auction_listing) VALUES ('3e113757-f0f9-4b7d-b347-3aeb56ebe768', 15, '4b0a488e-93c1-421e-8823-1459ac7acac8', 'd7ddc029-8f72-473e-83df-e97485bc360b');
INSERT INTO bid (id, bid_price, buyer, auction_listing) VALUES ('38c5119f-404b-45c3-a0da-95f3b2c3b449', 25, '4b0a488e-93c1-421e-8823-1459ac7acac8', 'd7ddc029-8f72-473e-83df-e97485bc360b');
