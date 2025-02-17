create database powerzone;

set search_path to powerzone;
set search_path = "public";

create table "users" (
                        id bigserial primary key,
                        email varchar(50) not null unique,
                        password varchar(100) not null,
                        role smallint not null default 0
);

create table "profile" (
                           id bigint primary key,
                           name varchar(50) not null,
                           nickname varchar(50) not null unique,
                           avatar varchar(200) not null default 'https://res.cloudinary.com/dflz0gveu/image/upload/v1718394870/avatars/default.png',
                           born_date date not null,
                           ban_at timestamp,

                           created_at timestamp not null default now(),
                           activo boolean not null default true,
                           constraint fk_profile_user foreign key (id) references "users" (id) on delete cascade

);

create table "post" (
                        id bigserial not null,
                        content text not null,
                        created_at timestamp not null default now(),
                        user_id bigint not null,
                        delete boolean,
                        primary key (id, created_at),
                        constraint fk_post_user foreign key (user_id) references "users" (id) on delete cascade
) partition by RANGE (created_at);

create index idx_post_content on post (content);

create table post_2025 partition of post
    for values from ('2025-01-01') to ('2026-01-01');

create table post_2026 partition of post
    for values from ('2026-01-01') to ('2027-01-01');

create table post_2027 partition of post
    for values from ('2027-01-01') to ('2028-01-01');

create table default_post partition of post
    default;

create index idx_post_created_at on post (created_at);

create table image (
    id bigserial not null,
    id_post bigint not null ,
    post_created_at timestamp not null,
    image varchar(200) not null,
    type smallint not null,
    primary key (id),
    constraint fk_image_post foreign key (id_post, post_created_at) references post (id, created_at) on delete cascade
);

create table comment (
                         id bigserial primary key,
                         content text not null,
                         created_at timestamp not null default now(),
                         user_id bigint not null,
                         post_id bigint not null,
                         post_created_at timestamp not null,
                         delete boolean not null default false,
                         constraint fk_comment_user foreign key (user_id) references "users" (id) on delete cascade,
                         constraint fk_comment_post foreign key (post_id, post_created_at) references post (id, created_at) on delete cascade
);

create table like_post (
                           user_id bigint not null,
                           post_id bigint not null,
                           created_at_post timestamp not null default now(),
                           constraint pk_like_post primary key (user_id, post_id),
                           constraint fk_like_post_user foreign key (user_id) references "users" (id) on delete cascade,
                           constraint fk_like_post_post foreign key (post_id, created_at_post) references post (id, created_at) on delete cascade
);

create table booksmarks (
                            user_id bigint not null,
                            post_id bigint not null,
                            created_at_post timestamp not null default now(),
                            constraint pk_booksmarks primary key (user_id, post_id),
                            constraint fk_booksmarks_user foreign key (user_id) references "users" (id) on delete cascade,
                            constraint fk_booksmarks_post foreign key (post_id, created_at_post) references post (id, created_at) on delete cascade
);

create table groupName (
                           id bigserial primary key,
                           name varchar(50) not null
);

create table groupUser (
                           id bigserial unique not null,
                           user_id bigint not null,
                           group_id bigint not null,
                           constraint pk_groupUser primary key (user_id, group_id),
                           constraint fk_groupUser_user foreign key (user_id) references "users" (id) on delete cascade,
                           constraint fk_groupUser_group foreign key (group_id) references groupName (id) on delete cascade
);

create table groupMessenger (
    id bigserial,
    message text not null,
    created_at timestamp not null default now(),
    grupoUser bigint not null,
    primary key (id, created_at),
    constraint fk_groupMessenger_groupUser foreign key (grupoUser) references groupUser (id) on delete cascade
) partition by RANGE (created_at);

create table groupMessenger_2024 partition of groupMessenger
    for values from ('2024-01-01') to ('2025-01-01');

create table groupMessenger_2025 partition of groupMessenger
    for values from ('2025-01-01') to ('2026-01-01');

create table groupMessenger_2026 partition of groupMessenger
    for values from ('2026-01-01') to ('2027-01-01');


create table reports (
    id bigserial primary key,
    type smallint not null,
    content text not null,
    created_at timestamp not null default now(),
    reporter bigint not null,
    post_id bigint not null,
    created_at_post timestamp not null,
    constraint fk_reports_reporter foreign key (reporter) references "users" (id) on delete cascade,
    constraint fk_reports_post foreign key (post_id, created_at_post) references post (id, created_at) on delete cascade
);

create table notification (
                              id bigserial primary key,
                              content text not null,
                              type smallint,
                              created_at timestamp not null default now(),
                              user_id bigint not null,
                              constraint fk_notification_user foreign key (user_id) references "users" (id) on delete cascade
);

ALTER TABLE profile ADD COLUMN is_new_user BOOLEAN DEFAULT TRUE;


CREATE TABLE follower (
                          profile_id BIGINT NOT NULL,
                          follower_id BIGINT NOT NULL,
                          CONSTRAINT pk_follower PRIMARY KEY (profile_id, follower_id),
                          CONSTRAINT fk_follower_profile FOREIGN KEY (profile_id) REFERENCES profile (id) ON DELETE CASCADE,
                          CONSTRAINT fk_follower_follower FOREIGN KEY (follower_id) REFERENCES profile (id) ON DELETE CASCADE
);


DROP TABLE IF EXISTS notification;

create table notification (
                              id bigserial primary key,
                              content bigint not null,
                              type smallint,
                              created_at timestamp not null default now(),
                              recibe_id bigint not null,
                              sender_id bigint not null,
                              constraint fk_notification_profile foreign key (recibe_id) references profile (id) on delete cascade,
                              constraint fk_notification_sender foreign key (sender_id) references profile (id) on delete cascade
);


CREATE TABLE search_history (
                                id SERIAL PRIMARY KEY,
                                profile_id BIGINT NOT NULL,
                                search_text VARCHAR(255) NOT NULL,
                                searched_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                FOREIGN KEY (profile_id) REFERENCES profile(id) ON DELETE CASCADE
);

ALTER TABLE groupname
    ADD COLUMN image VARCHAR(200) DEFAULT 'https://default.image.url';

