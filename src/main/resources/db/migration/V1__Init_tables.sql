CREATE TABLE client
(
    id     BIGINT NOT NULL PRIMARY KEY,
    name   VARCHAR(255),
    phone1 VARCHAR(255),
    phone2 VARCHAR(255)
);

CREATE TABLE application_user
(
    id                    BIGINT  NOT NULL PRIMARY KEY,
    application_user_role VARCHAR(255),
    client_id             BIGINT REFERENCES client,
    email                 VARCHAR(255),
    password              VARCHAR(255),
    locked                BOOLEAN NOT NULL,
    enabled               BOOLEAN NOT NULL,
    photo                 VARCHAR(255)
);

CREATE TABLE booking
(
    id              BIGINT    NOT NULL PRIMARY KEY,
    client_id       BIGINT REFERENCES client,
    date_of_arrival TIMESTAMP NOT NULL,
    date_of_return  TIMESTAMP NOT NULL,
    completed       BOOLEAN   NOT NULL
);

CREATE TABLE rider
(
    id        BIGINT           NOT NULL PRIMARY KEY,
    name      VARCHAR(255),
    sex       VARCHAR(255),
    height    DOUBLE PRECISION NOT NULL,
    weight    DOUBLE PRECISION NOT NULL,
    foot_size VARCHAR(255)
);

CREATE TABLE equipment
(
    id           BIGINT NOT NULL PRIMARY KEY,
    type         VARCHAR(255),
    name         VARCHAR(255),
    condition    VARCHAR(255),
    size         VARCHAR(255),
    stiffness    VARCHAR(255),
    arch         VARCHAR(255),
    binding_size VARCHAR(255)
);

CREATE TABLE booking_rider_equipment_link
(
    id         BIGINT NOT NULL PRIMARY KEY,
    booking_id BIGINT REFERENCES booking,
    rider_id   BIGINT REFERENCES rider
);

CREATE TABLE link_to_requested_equipment
(
    booking_rider_equipment_link_id BIGINT NOT NULL REFERENCES booking_rider_equipment_link,
    rider_requested_equipment       VARCHAR(255)
);

CREATE TABLE link_to_assigned_equipment
(
    booking_rider_equipment_link_id BIGINT NOT NULL REFERENCES booking_rider_equipment_link,
    rider_assigned_equipment_id     BIGINT NOT NULL REFERENCES equipment
);
