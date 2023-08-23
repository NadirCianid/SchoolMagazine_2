use school_magazine;

drop table teachers_classes;
drop table homeworks;
drop table marks;
drop table reproofs;
drop table teachers;
drop table students;
drop table classes;
drop table subjects;



create table classes (
class_name varchar(10) primary key
 );

create table subjects (
subject_id int primary key auto_increment,
subject_name varchar(20) not null);

create table teachers (
teacher_id int primary key auto_increment,
fio varchar(40) not null,
mail varchar(50) not null unique,
pswd varchar(20) not null,
personal_info varchar(100) );

create table teachers_classes (
record_id int primary key auto_increment,
teacher_id int not null,
class_name varchar(10) not null,
subject_id int not null,
constraint teachers_classes_1_fk foreign key (class_name)
references classes (class_name),
constraint teachers_classes_2_fk foreign key (subject_id)
references subjects (subject_id),
constraint teachers_classes_3_fk foreign key (teacher_id)
references teachers (teacher_id),
constraint teachers_classes_unique unique (class_name, subject_id, teacher_id)
);

create table students (
student_id int  primary key AUTO_INCREMENT ,
fio varchar(40) not null,
mail varchar(50) not null unique,
pswd varchar(20) not null,
class_name varchar(10) not null,
personal_info varchar(100),
constraint student_fk foreign key (class_name)
references classes (class_name));

CREATE UNIQUE INDEX UQ_students_mail ON students (mail);

create table homeworks (
homework_id int primary key auto_increment,
class_name varchar(10) not null,
subject_id int not null,
teacher_id int not null,
deadline date not null,
body varchar(255) not null,
constraint homeworks_1_fk foreign key (class_name)
references classes (class_name),
constraint homeworks_2_fk foreign key (subject_id)
references subjects (subject_id),
constraint homeworks_3_fk foreign key (teacher_id)
references teachers (teacher_id));

create table marks (
mark_id int primary key auto_increment,
student_id int not null,
teacher_id int not null,
subject_id int not null,
mark int not null,
date datetime not null,
constraint marks_1_fk foreign key (subject_id)
references subjects (subject_id),
constraint marks_2_fk foreign key (teacher_id)
references teachers (teacher_id),
constraint marks_3_fk foreign key (student_id)
references students (student_id) );

create table reproofs (
reproof_id int primary key auto_increment,
student_id int not null,
teacher_id int not null,
text varchar(255) not null,
constraint reproofs_1_fk foreign key (teacher_id)
references teachers (teacher_id),
constraint reproofs_2_fk foreign key (student_id)
references students (student_id) );


insert into classes (class_name)
	values ("5 A"), ("5 B"), ("6 A"), ("6 B");

insert into subjects (subject_name)
		values ("Информатика"), ("Математика");

insert into teachers (fio, mail, pswd, personal_info)
	values ("Нестеров Сергей А.", "nesterov@spbstu.ru", "1234", "Лучший!");

insert into teachers_classes (teacher_id, class_name, subject_id)
	values (1, "5 A", 1), (1, "5 B", 1), (1, "6 A", 1), (1, "6 B", 1);

insert into students  (fio, mail, pswd, class_name, personal_info)
	values ("Колесникова Наталья В.", "kolesnikova@edu.spbstu.ru", "1234", "6 A", "Ж") ;

insert into marks (student_id, teacher_id, subject_id, mark, date)
	values (1, 1, 1, 10, "10.10.2010"), (1,1,1,7,"11.09.2010"),
    (1, 1, 1, 5, "10.11.2010"), (1,1,1,7,"11.12.2010"),
    (1, 1, 1, 8, "10.1.2010"), (1,1,1,7,"11.2.2010"),
    (1, 1, 1, 8, "10.1.2010"), (1,1,1,7,"11.2.2010");

insert into marks (student_id, teacher_id, subject_id, mark, date)
	values (1, 1, 2, 10, "10.10.2010"), (1,1,2,7,"11.09.2010"),
    (1, 1, 2, 5, "10.11.2010"), (1,1,2,7,"11.12.2010"),
    (1, 1, 2, 8, "10.1.2010"), (1,1,2,7,"11.2.2010");

insert into homeworks (class_name, subject_id, teacher_id, deadline, body)
	values ("6 A", 1, 1, "11.2.2010", "Написать калькулятор на Java");

insert into reproofs (student_id, teacher_id, text)
	values (1, 1, "Слишком классно прогает!");


