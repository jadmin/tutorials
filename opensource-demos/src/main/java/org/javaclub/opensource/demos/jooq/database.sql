CREATE database test_jooq;

CREATE TABLE t_language (
  id int NOT NULL PRIMARY KEY,
  cd varchar(20) NOT NULL,
  description varchar(50)
);

CREATE TABLE t_author (
  id int NOT NULL PRIMARY KEY,
  first_name VARCHAR(50),
  last_name VARCHAR(50) NOT NULL,
  date_of_birth DATE,
  year_of_birth int
);

CREATE TABLE t_book (
  id int NOT NULL PRIMARY KEY,
  author_id int NOT NULL,
  title VARCHAR(400) NOT NULL,
  published_in int NOT NULL,
  language_id int NOT NULL,
  FOREIGN KEY (AUTHOR_ID) REFERENCES T_AUTHOR(ID),
  FOREIGN KEY (LANGUAGE_ID) REFERENCES T_LANGUAGE(ID)
);

CREATE TABLE t_book_store (
  name VARCHAR(200) NOT NULL UNIQUE
);

CREATE TABLE t_book_to_book_store (
  book_store_name VARCHAR(200) NOT NULL,
  book_id INTEGER NOT NULL,
  stock INTEGER,
  PRIMARY KEY(book_store_name, book_id),
  CONSTRAINT b2bs_book_store_id
    FOREIGN KEY (book_store_name)
    REFERENCES t_book_store (name)
    ON DELETE CASCADE,
  CONSTRAINT b2bs_book_id
    FOREIGN KEY (book_id)
    REFERENCES t_book (id)
    ON DELETE CASCADE
);