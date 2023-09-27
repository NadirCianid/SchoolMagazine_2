package Staff;

import Controllers.DataController;
import Entities.User;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

import static javax.swing.JOptionPane.PLAIN_MESSAGE;

public class Assistant {
    public static String[] getClassAndStudent(User user) throws SQLException {
        String[] selectedItems = new String[2];

        if(user.getRole().equals(UserRole.STUDENT)) {
            selectedItems[0] = DataController.getStudentClass(user.getName());
        } else {
            selectedItems[0] = selectClass();
        }
        selectedItems[1] = selectStudent(selectedItems[0]);

        return selectedItems;
    }

    private static String selectStudent(String selectedClass) throws SQLException {
        List<String> studentsIds = DataController.getStudentsFios(selectedClass);
        String result = "";
        try {
            result = (String) JOptionPane.showInputDialog(null,
                    "Выберите ученика",
                    "Выбор опорных элементов",
                    PLAIN_MESSAGE,
                    null,
                    studentsIds.toArray(),
                    studentsIds.get(0));
        } catch (IndexOutOfBoundsException e) {
            System.out.println("И такоге бывает)");
        }


        return result;
    }

    private static String selectClass() throws SQLException {
        List<String> classesIds = DataController.getClassesNames();

        return (String) JOptionPane.showInputDialog(null,
                "Выберите класс",
                "Выбор опорных элементов",
                PLAIN_MESSAGE,
                null,
                classesIds.toArray(),
                classesIds.get(0));
    }

    public static String[] getClassAndSubject(User user) throws SQLException {
        String[] selectedItems = new String[2];
        if(user.getRole().equals(UserRole.STUDENT)) {
            selectedItems[0] = DataController.getStudentClass(user.getName());
        } else {
            selectedItems[0] = selectClass();
        }

        selectedItems[1] = selectSubject();

        return selectedItems;
    }

    private static String selectSubject() throws SQLException {
        List<String> subjectNames = DataController.getSubjectNames();

        return (String) JOptionPane.showInputDialog(null,
                "Выберите предмет",
                "Выбор опорных элементов",
                PLAIN_MESSAGE,
                null,
                subjectNames.toArray(),
                subjectNames.get(0));
    }
}
