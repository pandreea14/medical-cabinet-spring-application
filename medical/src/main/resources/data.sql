# INSERT INTO java.specialization (name) VALUES
#                                            ('Cardiology'),
#                                            ('Dermatology'),
#                                            ('Neurology'),
#                                            ('Pediatrics');
#
# INSERT INTO java.doctor (first_name, last_name, specialization_id, email, phone) VALUES
#                                                                                      ('Andrei', 'Popescu', 1, 'andrei.popescu@clinic.ro', '0711111111'),
#                                                                                      ('Maria', 'Ionescu', 2, 'maria.ionescu@clinic.ro', '0722222222'),
#                                                                                      ('Ioan', 'Georgescu', 3, 'ioan.georgescu@clinic.ro', '0733333333'),
#                                                                                      ('Elena', 'Dumitrescu', 4, 'elena.dumitrescu@clinic.ro', '0744444444');
#
# INSERT INTO java.patient (first_name, last_name, cnp, email, phone) VALUES
#                                                                         ('Alex', 'Marin', '1960101123456', 'alex.marin@gmail.com', '0755555555'),
#                                                                         ('Bianca', 'Stan', '2980202123456', 'bianca.stan@gmail.com', '0766666666'),
#                                                                         ('Cristian', 'Voicu', '1950303123456', 'cristian.voicu@gmail.com', '0777777777');
#
# INSERT INTO java.appointment (patient_id, doctor_id, appointment_date, reason) VALUES
#                                                                                    (1, 1, '2025-01-10 10:00:00', 'Chest pain and fatigue'),
#                                                                                    (2, 2, '2025-01-11 11:30:00', 'Skin irritation'),
#                                                                                    (3, 3, '2025-01-12 09:15:00', 'Frequent headaches'),
#                                                                                    (1, 4, '2025-01-13 14:00:00', 'Child routine check');
#
# INSERT INTO java.prescription (appointment_id, issued_date, instructions) VALUES
#                                                                               (1, '2025-01-10 10:30:00', 'Take medication after meals'),
#                                                                               (2, '2025-01-11 12:00:00', 'Apply cream twice daily'),
#                                                                               (3, '2025-01-12 09:45:00', 'Avoid driving while medicated');
#
# INSERT INTO java.medication (name, description) VALUES
#                                                     ('Paracetamol', 'Pain reliever and fever reducer'),
#                                                     ('Ibuprofen', 'Anti-inflammatory medication'),
#                                                     ('Amoxicillin', 'Antibiotic'),
#                                                     ('Hydrocortisone Cream', 'Topical corticosteroid');
#
# INSERT INTO java.prescription_medication (prescription_id, medication_id, dosage) VALUES
#                                                                                       (1, 1, '500mg twice a day'),
#                                                                                       (1, 2, '400mg once a day'),
#                                                                                       (2, 4, 'Apply morning and evening'),
#                                                                                       (3, 1, '500mg every 8 hours'),
#                                                                                       (3, 3, '250mg twice a day');
