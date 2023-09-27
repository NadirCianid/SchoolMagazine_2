package Staff;

import Controllers.DataController;

import java.sql.SQLException;

import static Controllers.Application.user;

public class Test {
    public static void main(String[] args) throws SQLException {
        DataController.initializeDataBase();

       String[] sel = Assistant.getClassAndStudent(user);
        System.out.println(sel[0] + " " + sel[1]);
    }
}
