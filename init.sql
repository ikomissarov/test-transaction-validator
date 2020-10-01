create table transaction
(
    id         uuid           not null primary key,
    iban       varchar(255)   not null,
    amount     numeric(19, 2) not null,
    created_at timestamp      not null
);

create table account
(
    iban        varchar(255) not null primary key,
    blacklisted boolean      not null
);
