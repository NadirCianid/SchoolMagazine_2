-- create database financial_assistance;

use financial_assistance;

drop table FA_requests;
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
description varchar(500) not null);

create table fa_categories (
id int primary key auto_increment,
title varchar(500) not null,
payment_amount int not null);

create table required_documents_list (
id int primary key auto_increment,
required_document_id int not null,
constraint required_document_fk foreign key (required_document_id)
references required_documents (id), 
fa_category_id int not null,
constraint fa_category_fk foreign key (fa_category_id)
references fa_categories (id)
);

create table fa_requests (
id int primary key auto_increment,
filling_date date not null,
response_date date,
conf_doc_link varchar(200) not null,
request_status varchar(50) not null,
admin_coment varchar(500) ,
payment_amount int,
admin_id int,
constraint admin_fk foreign key (admin_id)
references admins (id), 
student_id int not null,
constraint fa_student_fk foreign key (student_id)
references students (id), 
fa_category_id int not null,
constraint fa_cat_fk foreign key (fa_category_id)
references fa_categories (id));

CREATE UNIQUE INDEX UQ_request_student ON fa_requests (student_id, fa_category_id);


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

insert into required_documents (description)
values ('Копии справок, выданных органами местного самоуправления'), 
('Сопроводительное письмо о чрезвычайном обстоятельстве'),
('Копия свидетельства о смерти члена Профсоюза, члена его семьи'),
('Свидетельство о рождении или копия решения суда об усыновлении'),
('Документы, подтверждающие родство (свидетельство о рождении, смене фамилии, браке)')  ;

insert into fa_categories (title, payment_amount)
values ('В связи с чрезвычайными обстоятельствами', 23700),
('В случае смерти члена Профсоюза, члена его семьи (родители, супруг(а) и дети)', 23700),
('В связи с рождением (усыновлением) ребенка', 16000);

insert into required_documents_list (required_document_id, fa_category_id) 
values (2, 1),
(1, 1),
(5, 2),
(3, 2),
(4, 3);






