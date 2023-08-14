package Controllers;

import java.sql.*;

public class DataContrloller {
    private static ResultSet resultSet;
    private static PreparedStatement loginStudentsCheckPS;
    private static PreparedStatement loginTeachersCheckPS;
    private static PreparedStatement selectTeachersFioPS;
    private static PreparedStatement selectStudentsFioPS;


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

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
