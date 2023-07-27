CREATE SCHEMA human_friends;
use human_friends;

-- Создаем и заполняем таблицы, согласно диаграмме.

CREATE TABLE Animals (
	id INT PRIMARY KEY AUTO_INCREMENT,
	type VARCHAR(30)
);

INSERT INTO Animals (type)
VALUES ('Домашние животные'),
	   ('Вьючные животные');
       
CREATE TABLE Pets (
	id INT PRIMARY KEY AUTO_INCREMENT,
	kind VARCHAR(30),
	pet_id INT,
	FOREIGN KEY (pet_id) REFERENCES Animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Pets (kind, pet_id)
VALUES ('Собаки', 1),
	   ('Кошки', 1),
	   ('Хомяки', 1);
       
       
CREATE TABLE Pack_Animals (
	id INT PRIMARY KEY AUTO_INCREMENT,
	kind VARCHAR(30),
	pa_id INT,
	FOREIGN KEY (pa_id) REFERENCES Animals(id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Pack_Animals (kind, pa_id)
VALUES ('Лошади', 2),
	   ('Верблюды', 2),
	   ('Ослы', 2);
       
CREATE TABLE Dogs (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(30),
	command VARCHAR(30),
	date_of_birth DATE,
	dog_id INT,
	FOREIGN KEY (dog_id) REFERENCES Pets (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Dogs (name, command, date_of_birth, dog_id)
VALUES ('Шарик', 'Сидеть', '2022-05-06', 1),
	   ('Бобик', 'Дай лапу', '2021-06-07', 1);
       
CREATE TABLE Cats (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(30),
	command VARCHAR(30),
	date_of_birth DATE,
	cat_id INT,
	FOREIGN KEY (cat_id) REFERENCES Pets (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Cats (name, command, date_of_birth, cat_id)
VALUES ('Снежок', 'Еда!', '2020-01-03', 2),
	   ('Пушок', 'Мяу!', '2023-02-10', 2);
       
CREATE TABLE Hamsters (
	id INT PRIMARY KEY AUTO_INCREMENT,
	name VARCHAR(30),
	command VARCHAR(30),
	date_of_birth DATE,
	hamster_id INT,
	FOREIGN KEY (hamster_id) REFERENCES Pets (id) ON DELETE CASCADE ON UPDATE CASCADE
);

INSERT INTO Hamsters (name, command, date_of_birth, hamster_id)
VALUES ('Кучка', 'Крути колесо', '2023-05-05', 3), 
	   ('Щекастый', 'Спрячь еду!', '2023-01-1', 3);

CREATE TABLE Horses ( 
id INT PRIMARY KEY AUTO_INCREMENT, 
name VARCHAR(30), 
command VARCHAR(30), 
date_of_birth DATE, 
horse_id INT, 
FOREIGN KEY (horse_id) REFERENCES Pack_Animals (id) ON DELETE CASCADE ON UPDATE CASCADE );

INSERT INTO Horses (name, command, date_of_birth, horse_id) 
VALUES ('Плотва', 'Шевелись', '2018-12-12', 1), 
	   ('Гроза', 'Галоп', '2019-03-08', 1);

CREATE TABLE Camels ( 
id INT PRIMARY KEY AUTO_INCREMENT, 
name VARCHAR(30), 
command VARCHAR(30), 
date_of_birth DATE, 
camel_id INT, 
FOREIGN KEY (camel_id) REFERENCES Pack_Animals (id) ON DELETE CASCADE ON UPDATE CASCADE );

INSERT INTO Camels (name, command, date_of_birth, camel_id) 
VALUES ('Мистер Плевок', 'Не плеваться!', '2017-09-09', 2), 
	   ('Зубастый', 'Не кусаться!', '2021-07-07', 2);

CREATE TABLE Donkeys ( 
id INT PRIMARY KEY AUTO_INCREMENT, 
name VARCHAR(30), 
command VARCHAR(30), 
date_of_birth DATE, 
donkey_id INT, 
FOREIGN KEY (donkey_id) REFERENCES Pack_Animals (id) ON DELETE CASCADE ON UPDATE CASCADE );

INSERT INTO Donkeys (name, command, date_of_birth, donkey_id) 
VALUES ('Трудяга', 'Потащили!', '2015-03-03', 3), 
	   ('Иа', 'Не грусти!', '2020-02-02', 3);
       
-- Удаляем из таблицы верблюдов

SET SQL_SAFE_UPDATES = 0;
DELETE FROM camels;

-- Объединяем таблицы Лошади и Ослы

SELECT * FROM Horses
UNION SELECT  * FROM Donkeys;
     
-- Создаем новую временную таблицу, на основе которой отфильтруем животных по возрасту
 
CREATE TEMPORARY TABLE all_animals AS 
SELECT *, 'Собаки' AS kind FROM Dogs
UNION SELECT *, 'Кошки' AS kind FROM Cats
UNION SELECT *, 'Хомяки' AS kind FROM Hamsters
UNION SELECT *, 'Лошади' AS kind FROM Horses
UNION SELECT *, 'Ослы' AS kind FROM Donkeys; 

SELECT * FROM all_animals;      
	
CREATE TABLE young_animals AS
SELECT name, command, date_of_birth, kind, TIMESTAMPDIFF(MONTH, date_of_birth, CURDATE()) AS Age_in_months
FROM all_animals WHERE date_of_birth BETWEEN ADDDATE(CURDATE(), INTERVAL -3 YEAR) AND ADDDATE(CURDATE(), INTERVAL -1 YEAR);
 
SELECT * FROM young_animals;

-- Объединяем таблицы, сохраняя поля, указывающие на прошлую принадлежность к старым таблицам.


SELECT Dogs.name, Dogs.date_of_birth, Dogs.command, Pets.kind, ya.Age_in_months 
FROM Dogs 
LEFT JOIN young_animals ya ON ya.name = Dogs.name
LEFT JOIN Pets ON Pets.id = dog_id
UNION
SELECT c.name, c.date_of_birth, c.command, Pets.kind, ya.Age_in_months 
FROM Cats c
LEFT JOIN young_animals ya ON ya.name = c.name
LEFT JOIN Pets ON Pets.id = cat_id
UNION
SELECT ham.name, ham.date_of_birth, ham.command, Pets.kind, ya.Age_in_months 
FROM Hamsters ham
LEFT JOIN young_animals ya ON ya.name = ham.name
LEFT JOIN Pets ON Pets.id = hamster_id
UNION
SELECT h.name, h.date_of_birth, h.command, pa.kind, ya.Age_in_months 
FROM Horses h
LEFT JOIN young_animals ya ON ya.name = h.name
LEFT JOIN Pack_Animals pa ON pa.id = horse_id
UNION 
SELECT d.name, d.date_of_birth, d.command, pa.kind, ya.Age_in_months 
FROM Donkeys d 
LEFT JOIN young_animals ya ON ya.name = d.name
LEFT JOIN Pack_Animals pa ON pa.id = donkey_id;



       



       