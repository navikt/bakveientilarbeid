CREATE TABLE IF NOT EXISTS profil (
    id  SERIAL NOT NULL PRIMARY KEY,
    bruker_id VARCHAR NOT NULL,
    profil JSON NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX profil_bruker_id_index ON profil (bruker_id);

