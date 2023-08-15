create table book
(
    id          serial,
    title       varchar(255) default 'Unknown'::character varying not null,
    description varchar(7500),
    file_url    varchar,
    constraint book_pk
        primary key (id)
);

create table role_of_user
(
    id   serial,
    name varchar(255) not null,
    constraint role_of_user_pk
        primary key (id)
);

create table account
(
    id          serial,
    role_id     integer default 2,
    username    varchar(255) not null,
    password    varchar(255) not null,
    email       varchar(255) not null,
    picture_url varchar(255),
    constraint account_pk
        primary key (id),
    constraint account_role_id_fk
        foreign key (role_id) references role_of_user
);

create unique index account_username_uindex
    on account (username);

create unique index account_email_uindex
    on account (email);

create table post
(
    id             serial,
    user_id        integer                                             not null,
    title          varchar(255) default 'not title'::character varying not null,
    date           timestamp,
    author_of_post varchar(255)                                        not null,
    text           varchar,
    constraint post_pk
        primary key (id),
    constraint post_user_id_fk
        foreign key (user_id) references account
);

create table comment
(
    id                     serial,
    user_id                integer       not null,
    post_id                integer       not null,
    response_to_comment_id integer,
    text                   varchar(5000) not null,
    date_time              timestamp,
    constraint comment_pk
        primary key (id),
    constraint comment_post_id_fk
        foreign key (post_id) references post,
    constraint comment_user_id_fk
        foreign key (user_id) references account
);
