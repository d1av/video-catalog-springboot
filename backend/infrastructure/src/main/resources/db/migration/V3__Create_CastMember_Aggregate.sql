CREATE TABLE cast_members (
        id VARCHAR(36) NOT NULL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        type VARCHAR(40) NOT NULL,
        created_At DATETIME(6) NOT NULL,
        updated_At DATETIME(6) NOT NULL
);