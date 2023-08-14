use school_magazine;

drop table teachers_classes;
drop table homeworks;
drop table marks;
drop table reproofs;
drop table students;
drop table teachers;
drop table classes;
drop table subjects;


create table classes (
class_id int primary key auto_increment,
class_name varchar(10) not null);

create table subjects (
subject_id int primary key auto_increment,
subject_name varchar(20) not null);

create table teachers (
teacher_id int primary key auto_increment,
fio varchar(40) not null,
mail varchar(20) not null,
pswd varchar(20) not null,
personal_info varchar(100) );

create table teachers_classes (
record_id int primary key auto_increment,
teacher_id int not null,
class_id int not null,
subject_id int not null,
constraint teachers_classes_1_fk foreign key (class_id)
references classes (class_id),
constraint teachers_classes_2_fk foreign key (subject_id)
references subjects (subject_id),
constraint teachers_classes_3_fk foreign key (teacher_id)
references teachers (teacher_id),
constraint teachers_classes_unique unique (class_id, subject_id, teacher_id)
);

create table students (
student_id int  primary key AUTO_INCREMENT , 
fio varchar(40) not null,
mail varchar(20) not null,
pswd varchar(20) not null,
class_id int not null,
personal_info varchar(100),
constraint student_fk foreign key (class_id)
references classes (class_id));

create table homeworks (
homework_id int primary key auto_increment,
class_id int not null,
subject_id int not null,
teacher_id int not null,
deadline date not null,
body varchar(255) not null,
constraint homeworks_1_fk foreign key (class_id)
references classes (class_id),
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
		values ("INFORMATIKA");
        
insert into teachers (fio, mail, pswd, personal_info)
	values ("Sergey Nesterov A.", "nesterov@spbstu.ru", "1234", "lubit gory");
    
insert into teachers_classes (teacher_id, class_id, subject_id) 
	values (1, 1, 1), (1, 2, 1), (1, 3, 1), (1, 4, 1);

insert into students  (fio, mail, pswd, class_id, personal_info) 
	values ("Denis Romanenco A.", "kynev@list.ru", "1234", 1, "male") ;
    
    
