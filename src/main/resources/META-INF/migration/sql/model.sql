CREATE TABLE questions
(
  id BIGSERIAL NOT NULL,
  text VARCHAR(512) NOT NULL UNIQUE,
  CONSTRAINT pk_questions PRIMARY KEY(id)
);


CREATE TABLE things
(
  id BIGSERIAL NOT NULL,
  name VARCHAR(45) NOT NULL UNIQUE,
  CONSTRAINT pk_things PRIMARY KEY(id)
);

CREATE TABLE relations
(
  id BIGSERIAL NOT NULL,
  type CHAR(1) NOT NULL CHECK (type = 'Y' OR type = 'M' OR type = 'N'),
  question_id BIGINT NOT NULL,
  thing_id BIGINT NOT NULL,
  CONSTRAINT pk_relations PRIMARY KEY(id),
  UNIQUE(question_id, thing_id),
  CONSTRAINT fk_relations_questions FOREIGN KEY (question_id) REFERENCES questions(id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_relations_things1 FOREIGN KEY (thing_id) REFERENCES things(id)
    ON DELETE CASCADE ON UPDATE CASCADE
);
