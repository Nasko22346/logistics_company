create table if not exists companies
(
    company_id          int          not null auto_increment primary key,
    company_name        varchar(255) not null,
    phone_number        varchar(50) not null,
    email               varchar(255) not null unique,
    company_eik         varchar(50) not null,
    company_description text
) engine=InnoDB default charset=utf8mb4;
