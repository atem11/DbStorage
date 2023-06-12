CREATE TABLE IF NOT EXISTS connections (
    connection_alias  varchar(255)    NOT NULL,
    connection_url    varchar(255)    NOT NULL,
    username          varchar(255)    NOT NULL,
    db_password       varchar(255)    NOT NULL,
    PRIMARY KEY(connection_alias)
);