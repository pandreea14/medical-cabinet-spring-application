CREATE TABLE IF NOT EXISTS `java`.`patient` (`id` INT NOT NULL AUTO_INCREMENT,
                               first_name VARCHAR(100) NOT NULL,
                               last_name VARCHAR(100) NOT NULL,
                               cnp VARCHAR(13) NOT NULL UNIQUE,
                               email VARCHAR(150),
                               phone VARCHAR(20),
                               PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `java`.`specialization` (`id` INT NOT NULL AUTO_INCREMENT,
                                      name VARCHAR(100) NOT NULL UNIQUE,
                                      PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `java`.`doctor` (`id` INT NOT NULL AUTO_INCREMENT,
                              first_name VARCHAR(100) NOT NULL,
                              last_name VARCHAR(100) NOT NULL,
                              specialization_id INT NOT NULL,
                              email VARCHAR(150),
                              phone VARCHAR(20),
                              PRIMARY KEY (`id`),
                              FOREIGN KEY (specialization_id) REFERENCES specialization(id)
);

CREATE TABLE IF NOT EXISTS `java`.`appointment` (`id` INT NOT NULL AUTO_INCREMENT,
                                   patient_id INT NOT NULL,
                                   doctor_id INT NOT NULL,
                                   appointment_date DATETIME NOT NULL,
                                   reason VARCHAR(255),
                                   PRIMARY KEY (`id`),
                                   FOREIGN KEY (patient_id) REFERENCES patient(id),
                                   FOREIGN KEY (doctor_id) REFERENCES doctor(id)
);

CREATE TABLE IF NOT EXISTS `java`.`prescription` (`id` INT NOT NULL AUTO_INCREMENT,
                                    appointment_id INT NOT NULL,
                                    issued_date DATETIME NOT NULL,
                                    instructions TEXT,
                                    PRIMARY KEY (`id`),
                                    FOREIGN KEY (appointment_id) REFERENCES appointment(id)
);

CREATE TABLE IF NOT EXISTS `java`.`medication` (`id` INT NOT NULL AUTO_INCREMENT,
                                  name VARCHAR(100) NOT NULL UNIQUE,
                                  description TEXT,
                                  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `java`.`prescription_medication` (`id` INT NOT NULL AUTO_INCREMENT,
                                                  prescription_id INT NOT NULL,
                                                  medication_id INT NOT NULL,
                                                  dosage VARCHAR(100),
                                                  PRIMARY KEY (`id`),
                                                  FOREIGN KEY (prescription_id) REFERENCES prescription(id),
                                                  FOREIGN KEY (medication_id) REFERENCES medication(id)
);
