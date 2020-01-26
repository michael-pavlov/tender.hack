create schema if not exists tender;

CREATE SEQUENCE if not exists tender.offers_offer_id_seq;
CREATE SEQUENCE if not exists tender.orders_order_id_seq;

CREATE TABLE if not exists tender.cte (
	cte_id bigint not null,
	cte_type VARCHAR(255) not null,
	name VARCHAR(255) not null,
	ram int not null,
	hdd int not null,
	resolution VARCHAR(20) not null,
  generation int not null,
  CONSTRAINT cte_id_pkey PRIMARY KEY (cte_id)
);

CREATE TABLE if not exists tender.offers (
  offer_id bigint NOT NULL DEFAULT nextval('tender.offers_offer_id_seq'::regclass),
  cte_id bigint not null,
	price numeric not null,
	discount numeric(2, 2) not null,
	vendor_id bigint,
	dicount_goods int not null,
	CONSTRAINT offers_pkey PRIMARY KEY (offer_id),
	CONSTRAINT offers_cte_id_foreign_key FOREIGN KEY (cte_id)
        REFERENCES tender.cte (cte_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE if not exists tender.orders (
  order_id bigint NOT NULL DEFAULT nextval('tender.orders_order_id_seq'::regclass),
	cte_id bigint not null,
	count_goods int not null,
	start_time bigint not null,
  stop_time bigint not null,
  end_time_order bigint not null,
  cooperative_id bigint,
  status varchar(30) not null,
  CONSTRAINT order_id_pkey PRIMARY KEY (order_id),
  CONSTRAINT orders_cte_id_foreign_key FOREIGN KEY (cte_id)
        REFERENCES tender.cte (cte_id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);