-- Insert data into Personne table
INSERT INTO Personne (id, nom, prenom, adresse, code_postal, email) VALUES
(1000, 'Dupont', 'Jean', '123 Rue de Paris', '75001', 'jean.dupont@example.com'),
(2000, 'Martin', 'Sophie', '456 Avenue de Lyon', '69001', 'sophie.martin@example.com'),
(3000, 'Durand', 'Pierre', '789 Boulevard de Marseille', '13001', 'pierre.durand@example.com');

-- Insert data into Voiture table
INSERT INTO Voiture (id, modele, marque, annee, type, chevaux_fiscaux, prix, consommation, couleur, proprietaire_id, locataire_id) VALUES
(1000, 'Model S', 'Tesla', 2020, 'BERLINE', 10, 80000, 15, 'Noir', 1000, 3000),
(2000, 'Civic', 'Honda', 2018, 'COMPACTE', 8, 20000, 8, 'Blanc', 1000, 3000),
(3000, 'Mustang', 'Ford', 2019, 'SPORTIVE', 12, 50000, 12, 'Rouge', 2000, 1000),
(4000, 'Clio', 'Renault', 2017, 'CITADINE', 6, 15000, 6, 'Bleu', 2000, 1000),
(5000, 'A3', 'Audi', 2016, 'COMPACTE', 9, 25000, 9, 'Gris', 3000, 2000),
(6000, 'Cayenne', 'Porsche', 2019, 'SUV', 15, 70000, 18, 'Noir', 3000, 2000);