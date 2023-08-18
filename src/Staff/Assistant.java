package Staff;

import Controllers.DataController;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public class Assistant {
    public static String[] getClassAndStudent() throws SQLException {
        List<String> classesIds = DataController.getClassesNames();

        String selectedClass = (String) JOptionPane.showInputDialog(null,
                "Выберите класс",
                "Выбор опорных элементов",
                PLAIN_MESSAGE,
                null,
                classesIds.toArray(),
                classesIds.get(0));

        List<String> studentsIds = DataController.getStudentsFios(selectedClass);

        String selectedStudent = (String) JOptionPane.showInputDialog(null,
                "Выберите ученика",
                "Выбор опорных элементов",
                PLAIN_MESSAGE,
                null,
                studentsIds.toArray(),
                studentsIds.get(0));

        String[] selectedItems = new String[2];
        selectedItems[0] = selectedClass;
        selectedItems[1] = selectedStudent;

        return selectedItems;
    }
}
