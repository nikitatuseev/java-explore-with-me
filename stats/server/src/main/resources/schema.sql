CREATE TABLE IF NOT EXISTS stats(
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    app varchar(128) NOT NULL,
    uri varchar(256) NOT NULL,
    ip varchar(64)  NOT NULL,
    date timestamp NOT NULL
);
