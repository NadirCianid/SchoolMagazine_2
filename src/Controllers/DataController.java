package Controllers;

import Entities.*;
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

    private static PreparedStatement selectAdminNamePS;
    private static PreparedStatement selectStudentsFioPS;


    // Новые поля
    private static PreparedStatement selectRequests;
    private static Statement selectRequestsForAdmin;


    private static Statement selectFACategories;
    private static PreparedStatement selectReqDocs;

    private static PreparedStatement insertRequest;

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
        selectAdminNamePS.setString(1, login);
        selectStudentsFioPS.setString(1, login);

        resultSet = selectAdminNamePS.executeQuery();
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
                    "FROM admins WHERE mail = ?  AND pswd = ?");

            selectAdminNamePS = conn.prepareStatement("select name from admins where mail = ?");
            selectStudentsFioPS = conn.prepareStatement("select name from students  where mail = ?");

            selectRequests = conn.prepareStatement("select fa_c.title, admin_coment, request_status, filling_date, response_date, fa_q.payment_amount \n" +
                    "from fa_requests as fa_q join fa_categories as fa_c on fa_q.FA_category_id = fa_c.id\n" +
                    "where student_id = (select id from students where mail = ?); ");

            selectRequestsForAdmin = conn.createStatement();

            selectFACategories = conn.createStatement();
            selectReqDocs = conn.prepareStatement("select rd.description from (required_documents_list as rdc join required_documents as rd on rdc.required_document_id = rd.id)\n" +
                    "join fa_categories on rdc.fa_category_id = fa_categories.id\n" +
                    "where title = ?;");

            insertRequest = conn.prepareStatement("insert into fa_requests (filling_date, conf_doc_link, request_status, student_id, fa_category_id )\n" +
                    "values (now(), ?, 'На рассмотрении', (select id from students where mail = ?), (select id from fa_categories where title = ?));");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static ObservableList<Request> getRequests(User user) throws SQLException {
        ObservableList<Request> requestsList = FXCollections.observableArrayList();

        switch (user.getRole()) {
            case ADMIN -> {
                getRequestsForAdminTable(user, requestsList);
            }
            case STUDENT -> {
                getRequestsForStudentTable(user, requestsList);
            }
        }

        return requestsList;
    }

    private static void getRequestsForAdminTable(User user, ObservableList<Request> requestsList) throws SQLException {
        resultSet = selectRequestsForAdmin.executeQuery("select conf_doc_link, title, students.name, institutes.name, specialties.name, filling_date\n" +
                "from (((fa_requests join fa_categories  on fa_requests.FA_category_id = fa_categories.id) \n" +
                "join students on fa_requests.student_id = students.id) \n" +
                "join specialties on students.specialty_id = specialties.id)\n" +
                "join institutes on specialties.institute_id = institutes.id; ");

        while (resultSet.next()) {
            String confDocLink = resultSet.getString(1);
            String categoryTitle = resultSet.getString(2);
            String studentName = resultSet.getString(3);
            String institute = resultSet.getString(4);
            String specialty = resultSet.getString(5);
            String fillingDate = resultSet.getDate(6)
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.getDefault()));

            Request newRequest = new Request(confDocLink, categoryTitle, fillingDate);

            newRequest.setStudentParams(studentName, institute, specialty);

            requestsList.add(newRequest);
        }
    }

    private static void getRequestsForStudentTable(User user, ObservableList<Request> requestsList) throws SQLException {
        selectRequests.setString(1, user.getLogin());
        resultSet = selectRequests.executeQuery();


        while (resultSet.next()) {
            String title = resultSet.getString(1);
            String comment = resultSet.getString(2);
            String status = resultSet.getString(3);
            String fillingDate = resultSet.getDate(4)
                    .toLocalDate()
                    .format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.getDefault()));

            Date responseDate = resultSet.getDate(5);
            String responseDateString = "";
            if (responseDate != null) {
                responseDateString = responseDate.toLocalDate()
                        .format(DateTimeFormatter.ofPattern("EEEE, dd.MM.yyyy", Locale.getDefault()));
            }

            String payment = resultSet.getString(6);


            requestsList.add(new Request(title, comment, status, fillingDate, responseDateString, payment));
        }
    }

    public static ObservableList<FACategory>getFACategories() throws SQLException {
        ObservableList<FACategory> faCategories = FXCollections.observableArrayList();

        resultSet = selectFACategories.executeQuery("select title, payment_amount from fa_categories;");

        while (resultSet.next()) {
            ArrayList<String> reqDocs = getReqDocs(resultSet.getString(1));

            int index = 0;

            faCategories.add(new FACategory(resultSet.getString(1), reqDocs.get(index), resultSet.getString(2)));

            while (index < reqDocs.size()-1) {
                index++;
                faCategories.add(new FACategory("", reqDocs.get(index), ""));
            }
            faCategories.add(new FACategory("", "", ""));
        }

        return faCategories;
    }

    public static ArrayList<String> getReqDocs(String categoryTitle) throws SQLException {
        ArrayList<String> reqDocs = new ArrayList<>();

        selectReqDocs.setString(1, categoryTitle);

        ResultSet localResultSet = selectReqDocs.executeQuery();

        while(localResultSet.next()) {
            reqDocs.add(localResultSet.getString(1));
        }

        return reqDocs;
    }

    public static void insertRequest(String link, FACategory faCategory, String login) throws SQLException {
        insertRequest.setString(1, link);
        insertRequest.setString(2, login);
        insertRequest.setString(3, faCategory.getTitle());

        insertRequest.executeUpdate();
    }

    public static String getReqDocsInString(String faCategory) throws SQLException {
        StringBuilder reqDocs = new StringBuilder();

        getReqDocs(faCategory).forEach(reqDoc -> reqDocs.append("-").append(reqDoc).append(";\n"));

        return reqDocs.toString();
    }
}
