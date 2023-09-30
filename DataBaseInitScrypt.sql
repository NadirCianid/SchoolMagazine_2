-- create database financial_assistance;

use financial_assistance;

drop table FA_applications;
drop table required_documents_list;
drop table students;
drop table specialties;
drop table institutes;
drop table admins;
drop table required_documents;
drop table FA_categories;



create table institutes (
id int primary key auto_increment,
name varchar(40) not null);

create table specialties (
id int primary key auto_increment,
name varchar(40) not null,
institute_id int not null,
constraint institute_id_fk foreign key (institute_id)
references institutes (id));

create table admins (
id int primary key auto_increment,
name varchar(40) not null,
mail varchar(50) not null unique,
pswd varchar(20) not null);

CREATE UNIQUE INDEX UQ_admins_mail ON admins (mail);

create table students (
id int  primary key AUTO_INCREMENT ,
name varchar(40) not null,
mail varchar(50) not null unique,
pswd varchar(20) not null,
specialty_id int not null,
constraint student_fk foreign key (specialty_id)
references specialties (id));

CREATE UNIQUE INDEX UQ_students_mail ON students (mail);

create table required_documents (
id int primary key auto_increment,
title varchar(100) not null,
description varchar(500) not null);

create table FA_categories (
id int primary key auto_increment,
title varchar(100) not null,
description varchar(500) not null,
payment_amount int not null);

create table required_documents_list (
id int primary key auto_increment,
required_document_id int not null,
constraint required_document_fk foreign key (required_document_id)
references required_documents (id), 
FA_category_id int not null,
constraint FA_category_fk foreign key (FA_category_id)
references FA_categories (id)
);

create table FA_applications (
id int primary key auto_increment,
conf_doc_link varchar(200) not null,
application_status varchar(50) not null,
admin_coment varchar(500) not null,
payment_amount int not null,
admin_id int not null,
constraint admin_fk foreign key (admin_id)
references admins (id), 
student_id int not null,
constraint FA_student_fk foreign key (student_id)
references students (id), 
FA_category_id int not null,
constraint FA_cat_fk foreign key (FA_category_id)
references FA_categories (id));


insert into institutes (name) values ('ИКНК'), ('ИПМЭиТ'), ('Физ-мех');

insert into specialties (name, institute_id) 
values ('5130902/00201', 1), 
('5132703/00101', 1), 
('5130902/00202', 1),
('3733802/00301', 2),
('3733802/02602', 2),
('3733806/00401', 2),
('5030102/30001', 3),
('5031503/30003', 3),
('5030103/30002', 3);

insert into students (name, mail, pswd, specialty_id)
values ('Тюрина Татьяна Владимирована', 'tiurina.t@edu.spbstu.ru', '1111', 1),
('Шкурская Елизавета Дмитриевна', 'shkurskaya.e@edu.spbstu.ru', '1111', 2),
('Косенкова Анна Алексеевна', 'kosenkova.a@edu.spbstu.ru', '1111', 3),
('Иванов Иван Иванович', 'ivanov.i@edu.spbstu.ru', '1111', 4),
('Петров Петр Петрович', 'petrov.p@edu.spbstu.ru', '1111', 7);

insert into admins (name, mail, pswd) 
values ('Сергеев Сергей Сергеевич', 'sergeev@spbstu.ru', '1111'),
('Соколов Олег Олегович', 'sokolov.oo@sbpstu.ru', '1111');

insert into required_documents (title, description)
values ('Справки', 'Копии справок, выданных органами местного самоуправления'), 
('Пояснительная записка', 'Сопроводительное письмо о чрезвычайном обстоятельстве'),
('Свидетельстово смерти', 'Копия свидетельства о смерти члена Профсоюза, члена его семьи'),
('Свидетельство о рождении', 'Свидетельство о рождении или копия решения суда об усыновлении'),
('Документы, подтверждающие родство', 'свидетельство о рождении, смене фамилии, браке')  ;

insert into FA_categories (title, description, payment_amount)
values ('В связи с чрезвычайными обстоятельствами', 'Чрезвычайные могут быть разными, поэтому каждый случай рассматривается индивидуально.', 23700),
('В случае смерти члена Профсоюза, члена его семьи ', 'Членами семьи считаются: родители, супруг(а) и дети', 23700),
('В связи с рождением (усыновлением) ребенка', '-', 16000);

insert into required_documents_list (required_document_id, FA_category_id) 
values (2, 1),
(1, 1),
(5, 2),
(3, 2);






