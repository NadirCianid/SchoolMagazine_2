package Controllers;

import Entities.HomeworkOrReproof;
import Entities.SubjectFinalGrade;
import Entities.SubjectGrade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataController {
    private static ResultSet resultSet;

    private static PreparedStatement loginStudentsCheckPS;
    private static PreparedStatement loginTeachersCheckPS;

    private static PreparedStatement selectTeachersFioPS;
    private static PreparedStatement selectStudentsFioPS;

    private static Statement selectClasses;
    private static PreparedStatement selectStudentClass;
    private static PreparedStatement selectStudentFios;

    private static PreparedStatement selectFinalGrades;
    private static PreparedStatement selectGrades;
    private static PreparedStatement selectHomework;
    private static PreparedStatement selectReproofs;

    private static PreparedStatement insertGrade;
    private static PreparedStatement insertHomework;
    private static PreparedStatement insertReproof;


    public static boolean isDataValid(String login, String pswd) throws SQLException {
        //Настройка и проверка PreparedStatement учетной записи среди учеников
        loginStudentsCheckPS.setString(1, login);
        loginStudentsCheckPS.setString(2, pswd);

        resultSet = loginStudentsCheckPS.executeQuery();
        resultSet.next();
        int isStudent = resultSet.getInt(1);

        //Настройка и проверка PreparedStatement учетной записи среди учителей
        loginTeachersCheckPS.setString(1, login);
        loginTeachersCheckPS.setString(2, pswd);

        resultSet = loginTeachersCheckPS.executeQuery();
        resultSet.next();
        int isTeacher = resultSet.getInt(1);

        //установка нужного статуса в объекте студента. Метод возвращает true, если уч. запись существует.
        return Application.user.setRole(isStudent, isTeacher);
    }


    public static String getName(String login) throws SQLException {
        String name1;   //name from teachers table
        String name2;    //name from students table

        //Настройка PreparedStatement для получения имени пользователя
        selectTeachersFioPS.setString(1, login);
        selectStudentsFioPS.setString(1, login);

        resultSet = selectTeachersFioPS.executeQuery();
        if(!resultSet.next()) {
            name1 = "";
        } else {
            name1 =  resultSet.getString(1);
        }


        resultSet = selectStudentsFioPS.executeQuery();
        if(!resultSet.next()) {
            name2 = "";
        } else {
            name2 =  resultSet.getString(1);
        }

        return  name1 + name2; //вернется либо имя из таблицы учеников, либо имя из таблицы учителей
    }

    public static void initializeDataBase() throws SQLException {
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/financial_assistance?&serverTimezone=UTC";
        String user = "root";
        String password = "root";
        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, user, password);

            loginStudentsCheckPS = conn.prepareStatement("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END\n" +
                    "FROM students WHERE mail = ?  AND pswd = ?");

            loginTeachersCheckPS = conn.prepareStatement("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END\n" +
                    "FROM teachers WHERE mail = ?  AND pswd = ?");

            selectTeachersFioPS = conn.prepareStatement("select fio from teachers where mail = ?");
            selectStudentsFioPS = conn.prepareStatement("select fio from students  where mail = ?");

            selectClasses = conn.createStatement();
            selectStudentClass = conn.prepareStatement("select class_name from students where fio = ?");
            selectStudentFios = conn.prepareStatement("select fio from students where class_name = ?");

            selectFinalGrades = conn.prepareStatement("SELECT subject_name,\n" +
                    "    CASE \n" +
                    "        WHEN EXTRACT(MONTH FROM date) IN (9, 10) THEN 1  -- Первая четверть\n" +
                    "        WHEN EXTRACT(MONTH FROM date) IN (11, 12) THEN 2   -- Вторая четверть\n" +
                    "        WHEN EXTRACT(MONTH FROM date) IN (1, 2, 3) THEN 3    -- Третья четверть\n" +
                    "        WHEN EXTRACT(MONTH FROM date) IN (4, 5) THEN 4    -- Четвертая четверть\n" +
                    "    END AS номер_четверти,\n" +
                    "    AVG(mark) AS средняя_оценка\n" +
                    "FROM ( marks join students) join subjects \n" +
                    "where fio = ?" +
                    "GROUP BY subject_name, номер_четверти;");

            selectGrades = conn.prepareStatement("select students.fio, date, teachers.fio, mark " +
                    "from (((marks join students) join teachers) join subjects)\n" +
                    "where subject_name = ?" +
                    "and students.class_name = ?");

            selectHomework = conn.prepareStatement("select deadline, teachers.fio, body , subject_id\n" +
                    "                    from (homeworks join teachers)\n" +
                    "                    where class_name = ? \n" +
                    "                    and subject_id = (select subject_id from subjects where subject_name = ?);");

            selectReproofs = conn.prepareStatement("select teachers.fio, text \n" +
                    "from ((reproofs join teachers) join students) \n" +
                    "where students.fio = ?");

            insertGrade = conn.prepareStatement("insert into marks (student_id, teacher_id, subject_id, mark, date)\n" +
                    "\tvalues ((select student_id from students where fio = ?)\n" +
                    "\t\t  , (select teacher_id from teachers where fio = ?)\n" +
                    "          , (select subject_id from subjects where subject_name = ?), ?, current_date())");

            insertHomework = conn.prepareStatement("insert into homeworks (class_name, subject_id, teacher_id, deadline, body) \n" +
                    "\tvalues (?\n" +
                    "    , (select subject_id from subjects where subject_name = ?)\n" +
                    "    , (select teacher_id from teachers where fio = ?)\n" +
                    "    , ?, ?);");

            insertReproof = conn.prepareStatement("insert into reproofs (student_id, teacher_id, text) \n" +
                    "\tvalues ((select student_id from students where fio = ?)\n" +
                    "    , (select teacher_id from teachers where fio = ?), ?);");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getClassesNames() throws SQLException {
        ObservableList<String> classesNames = FXCollections.observableArrayList();

        resultSet = selectClasses.executeQuery("select class_name from classes;");

        while (resultSet.next()) {
            classesNames.add(resultSet.getString(1));
        }

        return classesNames;
    }

    public static ObservableList<String> getStudentsFios(String selectedClass) throws SQLException {
        ObservableList<String> studentsFios = FXCollections.observableArrayList();

        selectStudentFios.setString(1, selectedClass);
        resultSet = selectStudentFios.executeQuery();

        while (resultSet.next()) {
            studentsFios.add(resultSet.getString(1));
        }

        return studentsFios;
    }

    public static ObservableList<SubjectFinalGrade> getSubjectFinalGrades(String studentName) throws SQLException {
        ObservableList<SubjectFinalGrade> subjectFinalGrades = FXCollections.observableArrayList();

        //Мапа заполняется  по принципу: если есть ключ, то добавляем инфу в значение, а если нет, то добавляем новую пару.
        Map<String, SubjectFinalGrade> subjectFinalGradeMap = new HashMap<>();

        selectFinalGrades.setString(1, studentName);
        resultSet = selectFinalGrades.executeQuery();

        while (resultSet.next()) {
             String subjectName = resultSet.getString(1);
             int quarterNumber = resultSet.getInt(2);
             float quarterMark = resultSet.getFloat(3);

             if(subjectFinalGradeMap.containsKey(subjectName)) {
                 subjectFinalGradeMap.get(subjectName).addQuarterMark(quarterNumber, quarterMark);
             } else {
                 subjectFinalGradeMap.put(subjectName, new SubjectFinalGrade(subjectName, quarterNumber, quarterMark));
             }
        }

        subjectFinalGrades.addAll(subjectFinalGradeMap.values());
        subjectFinalGrades.sort(Comparator.comparingInt(SubjectFinalGrade::getNumber));

        return subjectFinalGrades;
    }

    public static ObservableList<String> getSubjectNames() throws SQLException {
        ObservableList<String> subjectNames = FXCollections.observableArrayList();

        resultSet = selectClasses.executeQuery("select subject_name from subjects;");

        while (resultSet.next()) {
            subjectNames.add(resultSet.getString(1));
        }

        return subjectNames;
    }

    public static ObservableList<SubjectGrade> getSubjectGrades(String[] selectedItems) throws SQLException {
        ObservableList<SubjectGrade> subjectGrades = FXCollections.observableArrayList();

        selectGrades.setString(1, selectedItems[1]);
        selectGrades.setString(2, selectedItems[0]);
        resultSet = selectGrades.executeQuery();


        while (resultSet.next()) {
            String studentName = resultSet.getString(1);
            String date = resultSet.getDate(2)
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.getDefault()));
            String teacher = resultSet.getString(3);
            int mark = resultSet.getInt(4);

            subjectGrades.add(new SubjectGrade(studentName, date, teacher, mark));
        }

        return subjectGrades;
    }

    public static ObservableList<HomeworkOrReproof> getHomework(String[] selectedItems) throws SQLException {
        ObservableList<HomeworkOrReproof> homeworkList = FXCollections.observableArrayList();

        selectHomework.setString(1, selectedItems[0]);
        selectHomework.setString(2, selectedItems[1]);
        resultSet = selectHomework.executeQuery();


        while (resultSet.next()) {
            String deadline = resultSet.getDate(1)
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.getDefault()));

            String teacher = resultSet.getString(2);
            String body = resultSet.getString(3);


            homeworkList.add(new HomeworkOrReproof(deadline, teacher, body));
        }

        return homeworkList;
    }

    public static ObservableList<HomeworkOrReproof> getReproofs(String selectedStudent) throws SQLException {
        ObservableList<HomeworkOrReproof> reproofsList = FXCollections.observableArrayList();

        selectReproofs.setString(1, selectedStudent);
        resultSet = selectReproofs.executeQuery();


        while (resultSet.next()) {
            String teacher = resultSet.getString(1);
            String body = resultSet.getString(2);


            reproofsList.add(new HomeworkOrReproof("", teacher, body));
        }

        return reproofsList;
    }

    public static void insertGrade(String student, String teacher, String subject, int grade) throws SQLException {
        insertGrade.setString(1, student);
        insertGrade.setString(2, teacher);
        insertGrade.setString(3, subject);
        insertGrade.setInt(4, grade);

        insertGrade.executeUpdate();
    }

    public static void insertHomework(String className, String subject, String teacher, LocalDate deadline, String homeworkText) throws SQLException {
        insertHomework.setString(1, className);
        insertHomework.setString(2, subject);
        insertHomework.setString(3, teacher);
        insertHomework.setDate(4, Date.valueOf(deadline));
        insertHomework.setString(5, homeworkText);

        insertHomework.executeUpdate();
    }

    public static void insertReproof(String student, String teacher, String reproofText) throws SQLException {
        insertReproof.setString(1, student);
        insertReproof.setString(2, teacher);
        insertReproof.setString(3, reproofText);

        insertReproof.executeUpdate();
    }

    public static String getStudentClass(String studentName) throws SQLException {
        selectStudentClass.setString(1, studentName);

        resultSet = selectStudentClass.executeQuery();
        resultSet.next();

        return resultSet.getString(1);
    }
}
