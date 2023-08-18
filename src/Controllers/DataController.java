package Controllers;

import Entities.SubjectFinalGrade;
import Entities.SubjectGrade;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DataController {
    private static ResultSet resultSet;

    private static PreparedStatement loginStudentsCheckPS;
    private static PreparedStatement loginTeachersCheckPS;

    private static PreparedStatement selectTeachersFioPS;
    private static PreparedStatement selectStudentsFioPS;

    private static Statement select;
    private static PreparedStatement selectStudentFios;

    private static PreparedStatement selectFinalGrades;
    private static PreparedStatement selectGrades;


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
        return TestApplication.user.setRole(isStudent, isTeacher);
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
        String url = "jdbc:mysql://localhost:3306/school_magazine?&serverTimezone=UTC";
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

            select = conn.createStatement();
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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String> getClassesNames() throws SQLException {
        List<String> classesNames = new ArrayList<>();

        resultSet = select.executeQuery("select class_name from classes;");

        while (resultSet.next()) {
            classesNames.add(resultSet.getString(1));
        }

        return classesNames;
    }

    public static List<String> getStudentsFios(String selectedClass) throws SQLException {
        List<String> studentsFios = new ArrayList<>();

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

    public static List<String> getSubjectNames() throws SQLException {
        List<String> subjectNames = new ArrayList<>();

        resultSet = select.executeQuery("select subject_name from subjects;");

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
}
