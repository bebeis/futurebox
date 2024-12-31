-- ===================================
-- 1) Future Movie Types
-- ===================================
CREATE TABLE public.future_movie_types (
                                           id               SERIAL         PRIMARY KEY,
                                           name             VARCHAR(100)   NOT NULL,
                                           description      VARCHAR(255)   NOT NULL,
                                           image_url        VARCHAR(255)   NOT NULL,
                                           detail_image_url VARCHAR(255)
);

-- ===================================
-- 2) Future Gifticon Types
-- ===================================
CREATE TABLE public.future_gifticon_types (
                                              id               SERIAL         PRIMARY KEY,
                                              name             VARCHAR(100)   NOT NULL,
                                              description      VARCHAR(255)   NOT NULL,
                                              image_url        VARCHAR(255)   NOT NULL,
                                              detail_image_url VARCHAR(255)
);

-- ===================================
-- 3) Future Invention Types
-- ===================================
CREATE TABLE public.future_invention_types (
                                               id               SERIAL         PRIMARY KEY,
                                               name             VARCHAR(100)   NOT NULL,
                                               description      VARCHAR(255)   NOT NULL,
                                               image_url        VARCHAR(255)   NOT NULL,
                                               detail_image_url VARCHAR(255)
);

-- ===================================
-- 4) Future Box
-- ===================================
CREATE TABLE public.future_box (
                                   id                     INTEGER           PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
                                   uuid                   UUID              NOT NULL UNIQUE,
                                   receiver               VARCHAR(100)      NOT NULL,
                                   sender                 VARCHAR(100)      NOT NULL,
                                   is_opened              BOOLEAN           DEFAULT FALSE,
                                   future_movie_type      INTEGER,
                                   future_gifticon_type   INTEGER,
                                   future_invention_type  INTEGER,
                                   created_at             TIMESTAMP         DEFAULT CURRENT_TIMESTAMP,

                                   CONSTRAINT fk_future_movie_type
                                       FOREIGN KEY (future_movie_type)
                                           REFERENCES public.future_movie_types (id)
                                           ON UPDATE NO ACTION
                                           ON DELETE NO ACTION,

                                   CONSTRAINT fk_future_gifticon_type
                                       FOREIGN KEY (future_gifticon_type)
                                           REFERENCES public.future_gifticon_types (id)
                                           ON UPDATE NO ACTION
                                           ON DELETE NO ACTION,

                                   CONSTRAINT fk_future_invention_type
                                       FOREIGN KEY (future_invention_type)
                                           REFERENCES public.future_invention_types (id)
                                           ON UPDATE NO ACTION
                                           ON DELETE NO ACTION
);

-- ===================================
-- 5) Future Face Mirror
-- ===================================
CREATE TABLE public.future_face_mirror (
                                           id        SERIAL      PRIMARY KEY,
                                           box_id    INTEGER,
                                           year      INTEGER,
                                           image_url TEXT        NOT NULL,

                                           CONSTRAINT future_face_mirror_box_id_fkey
                                               FOREIGN KEY (box_id)
                                                   REFERENCES public.future_box (id)
                                                   ON UPDATE NO ACTION
                                                   ON DELETE NO ACTION
);

-- ===================================
-- 6) Future Hologram
-- ===================================
CREATE TABLE public.future_hologram (
                                        id        SERIAL      PRIMARY KEY,
                                        box_id    INTEGER,
                                        message   TEXT,
                                        image_url TEXT        NOT NULL,

                                        CONSTRAINT future_hologram_box_id_fkey
                                            FOREIGN KEY (box_id)
                                                REFERENCES public.future_box (id)
                                                ON UPDATE NO ACTION
                                                ON DELETE NO ACTION
);

-- ===================================
-- 7) Future Lotto
-- ===================================
CREATE TABLE public.future_lotto (
                                     id      SERIAL      PRIMARY KEY,
                                     box_id  INTEGER,
                                     numbers INTEGER[]   NOT NULL,

                                     CONSTRAINT future_lotto_box_id_fkey
                                         FOREIGN KEY (box_id)
                                             REFERENCES public.future_box (id)
                                             ON UPDATE NO ACTION
                                             ON DELETE NO ACTION
);

-- ===================================
-- 8) Future Note
-- ===================================
CREATE TABLE public.future_note (
                                    id      SERIAL      PRIMARY KEY,
                                    box_id  INTEGER,
                                    message TEXT        NOT NULL,

                                    CONSTRAINT future_note_box_id_fkey
                                        FOREIGN KEY (box_id)
                                            REFERENCES public.future_box (id)
                                            ON UPDATE NO ACTION
                                            ON DELETE NO ACTION
);
