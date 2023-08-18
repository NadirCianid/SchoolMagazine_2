package Staff;

import Controllers.DataController;

import java.sql.SQLException;

public class Test {
    public static void main(String[] args) throws SQLException {
        DataController.initializeDataBase();

       String[] sel = Assistant.getClassAndStudent();
        System.out.println(sel[0] + " " + sel[1]);
    }
}
