create table if not exists users(
    id  integer generated by default as identity,
    email varchar(254) not null unique,
    name varchar(250) not null unique,
    constraint pk_users primary key (id)
);

create table if not exists categories(
    id integer generated by default as identity,
    name varchar(200) not null unique ,
    constraint pk_categories primary key (id)
);

create table if not exists locations(
    id integer generated by default as identity,
    lat real not null,
    lon real not null,
    constraint pk_locations primary key(id)
);

create table if not exists events(
    id integer generated by default as identity,
    annotation varchar(2000) not null,
    category_id integer not null references categories(id) on delete restrict,
    confirmer_requests integer default 0,
    created_on timestamp,
    description varchar(7000) not null,
    event_date timestamp not null,
    initiator_id integer not null references users(id) on update cascade on delete cascade,
    location_id integer not null references locations(id) on update cascade on delete cascade,
    paid boolean default false not null,
    participant_limit integer default 0,
    published_on timestamp,
    request_moderation boolean default true not null,
    state varchar(100),
    title varchar(120),
    constraint pk_events primary key(id)
);

create table if not exists requests(
   id integer generated by default as identity,
   event_id integer not null references events(id) on update cascade on delete cascade,
   requester_id integer not null references users(id) on update cascade on delete cascade,
   status varchar(100) not null,
   created timestamp not null,
   constraint pk_requests primary key (id)
);

create table if not exists compilations(
    id integer generated by default as identity,
    pinned boolean default false,
    title varchar(50) not null,
    constraint pk_compilations primary key (id)
);

create table if not exists compilations_events(
    event_id integer references events(id),
    compilation_id integer references compilations(id),
    constraint pk_compilations_events primary key (event_id, compilation_id)
);

create table if not exists comments(
    id integer generated by default as identity,
    text varchar(1000) not null,
    event_id integer not null references events(id) on update cascade on delete cascade,
    creator_id integer not null references users(id) on update cascade on delete cascade,
    created_on timestamp not null,
    last_edited_on timestamp,
    constraint pk_comment primary key(id)
);
