# Purchases Schema

# --- !Ups


CREATE SEQUENCE item_id_seq;
CREATE TABLE item (
	id integer NOT NULL DEFAULT nextval('item_id_seq'),
	description varchar(255) NOT NULL,
	price integer NOT NULL,
	PRIMARY KEY (id)
);

CREATE SEQUENCE merchant_id_seq;
CREATE TABLE merchant (
	id integer NOT NULL DEFAULT nextval('merchant_id_seq'),
	address varchar(255) NOT NULL,
	name varchar(255) NOT NULL,
	PRIMARY KEY (id)
);

CREATE SEQUENCE purchaser_id_seq;
CREATE TABLE purchaser (
	id integer NOT NULL DEFAULT nextval('purchaser_id_seq'),
	name varchar(255) NOT NULL,
	PRIMARY KEY (id)
);

CREATE SEQUENCE purchase_order_id_seq;
CREATE TABLE purchase_order (
	id integer NOT NULL DEFAULT nextval('purchase_order_id_seq'),
	item_id integer NOT NULL,
	purchaser_id integer NOT NULL,
	merchant_id integer NOT NULL,
	purchase_count integer NOT NULL,
	FOREIGN KEY (item_id) REFERENCES item(id),
	FOREIGN KEY (merchant_id) REFERENCES merchant(id),
	FOREIGN KEY (purchaser_id) REFERENCES purchaser(id),
	PRIMARY KEY (id)
);

# --- !Downs

DROP TABLE item;
DROP SEQUENCE item_id_seq;

DROP TABLE merchant;
DROP SEQUENCE merchant_id_seq;

DROP TABLE order;
DROP SEQUENCE order_id_seq;

DROP TABLE purchaser;
DROP SEQUENCE purchaser_id_seq;

