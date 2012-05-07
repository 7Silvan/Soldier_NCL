
DELETE  FROM Soldier;
DELETE  FROM Unit;
DELETE  FROM Location;
--DELETE * FROM Heads;

--('Loc_id', name, region, city)
INSERT INTO Location VALUES (1, 'Forest', 'East', 'Sumy');
INSERT INTO Location  VALUES (2, 'Steppe', 'Middle', 'Kiev');
INSERT INTO Location  VALUES (3, 'Rock', 'West', 'Ternopil');

--(Unit_id, name, location, headOfUnit)
INSERT INTO Unit VALUES (1, 'Robin', 1, null);
INSERT INTO Unit VALUES (2, 'Seagull', 2, null);
INSERT INTO Unit VALUES (3, 'Wolverine', 3, null);

--(Soldier_id, name, rank, commander, unit, birthDate, headOfUnit)
INSERT INTO Soldier VALUES (NULL, 'Gump', 'Captain', NULL, 1, TO_DATE('1981-11-17', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Roger', 'Sergant', 1000001, 1, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Thomas', 'Sergant', 1000001, 1, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Joe', 'Sergant', 1000001, 1, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'John', 'Sergant', 1000001, 1, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Lucky', 'Private', 1000005, 1, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Filippe', 'Private', 1000005, 1, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Roger2', 'Sergant', 1000001, 2, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Thomas2', 'Sergant', 1000001, 2, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Joe2', 'Sergant', 1000001, 2, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'John2', 'Sergant', 1000001, 2, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Lucky2', 'Private', 1000005, 2, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Filippe2', 'Private', 1000005, 2, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Roger3', 'Sergant', 1000001, 3, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Thomas3', 'Sergant', 1000001, 3, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Joe3', 'Sergant', 1000001, 3, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'John3', 'Sergant', 1000001, 3, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Lucky3', 'Private', 1000005, 3, TO_DATE('1981-11-13', 'YYYY-MM-DD'));
INSERT INTO Soldier VALUES (NULL, 'Filippe3', 'Private', 1000005, 3, TO_DATE('1981-11-13', 'YYYY-MM-DD'));

UPDATE Unit SET headOfUnit = 1000001 WHERE Unit_id = 1;
UPDATE Unit SET headOfUnit = 1000009 WHERE Unit_id = 2;
UPDATE Unit SET headOfUnit = 1000017 WHERE Unit_id = 3;

commit;