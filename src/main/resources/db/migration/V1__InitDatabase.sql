CREATE TABLE doctor (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR (50),
    email VARCHAR(100),
    phone_number VARCHAR(10),
    address VARCHAR (100),
    specialization VARCHAR(30),
    age INTEGER,
    skills VARCHAR(30) ARRAY,
    biography TEXT,
    department VARCHAR(40),
    role VARCHAR(20),
    password TEXT NOT NULL
);

CREATE TABLE patient (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    name VARCHAR(50),
    address VARCHAR (100),
    phone_number VARCHAR(10),
    email VARCHAR (50),
    age INTEGER,
    blood_group VARCHAR(3),
    religion VARCHAR(20),
    occupation VARCHAR(30),
    gender CHAR(1),
    marital_status VARCHAR(10),
    description TEXT,
    role VARCHAR(20),
    password TEXT NOT NULL,
    doctor_id BIGSERIAL
);

CREATE TABLE availability (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    doctor_id BIGSERIAL NOT NULL, --Rel
    day_of_week VARCHAR(10),
    start_time TIME,
    end_time TIME
);

CREATE TABLE appointment (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    doctor_id BIGSERIAL NOT NULL, --Rel
    patient_id BIGSERIAL NOT NULL, --Rel
    appointment_date DATE,
    start_time TIME,
    end_time TIME,
    reason TEXT,
    medical_record_id BIGSERIAL
);

CREATE TABLE prescription (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    medical_record_id BIGSERIAL NOT NULL, --Rel
    medication VARCHAR (40),
    start_date DATE,
    end_date DATE,
    dosage integer,
    total NUMERIC
);

CREATE TABLE medical_record (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    doctor_id BIGSERIAL NOT NULL, --Rel
    patient_id BIGSERIAL NOT NULL, --Rel
    appointment_id BIGSERIAL, --Rel
    check_in_date DATE,
    notes TEXT,
    disease VARCHAR(40),
    status VARCHAR(15),
    room_no VARCHAR(6)
);

CREATE TABLE expense (
    id BIGSERIAL NOT NULL PRIMARY KEY,
    patient_id BIGSERIAL, --Rel
    name VARCHAR(30),
    category VARCHAR(30),
    description TEXT,
    amount NUMERIC,
    date_of_expense DATE,
    paid BOOLEAN
);

ALTER TABLE patient ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id);

ALTER TABLE availability ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id) ON DELETE CASCADE;

ALTER TABLE appointment ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
                        ADD CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
                        ADD CONSTRAINT fk_medical FOREIGN KEY (medical_record_id) REFERENCES medical_record(id);

ALTER TABLE prescription ADD CONSTRAINT fk_medical FOREIGN KEY (medical_record_id) REFERENCES medical_record(id)
ON UPDATE CASCADE;

ALTER TABLE medical_record ADD CONSTRAINT fk_doctor FOREIGN KEY (doctor_id) REFERENCES doctor(id),
                           ADD CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patient(id),
                           ADD CONSTRAINT fk_appointment FOREIGN KEY (appointment_id) REFERENCES appointment(id);

ALTER TABLE expense ADD CONSTRAINT fk_patient FOREIGN KEY (patient_id) REFERENCES patient(id);

ALTER TABLE appointment ALTER COLUMN medical_record_id DROP NOT NULL;
ALTER TABLE medical_record ALTER COLUMN appointment_id DROP NOT NULL;