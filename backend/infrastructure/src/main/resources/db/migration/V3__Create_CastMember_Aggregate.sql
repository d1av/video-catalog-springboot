CREATE TABLE cast_members (
        id CHAR(32) NOT NULL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        type VARCHAR(40) NOT NULL,
        created_At DATETIME(6) NOT NULL,
        updated_At DATETIME(6) NOT NULL
);