package Entities;


public class SubjectGrade {
    private static int currentNumber = 0; //счетчик количества текущих оценок
    private int gradeNumber;
    private String student;
    private String date;
    private String gradeTeacher;
    private int gradeMark;

    public SubjectGrade(String student, String date, String teacher, int mark) {
        this.gradeNumber = ++currentNumber;
        this.student = student;
        this.date = date;
        this.gradeTeacher = teacher;
        this.gradeMark = mark;
    }

    public static int getCurrentNumber() {
        return currentNumber;
    }

    public int getGradeNumber() {
        return gradeNumber;
    }

    public String getStudent() {
        return student;
    }

    public String getDate() {
        return date;
    }

    public String getGradeTeacher() {
        return gradeTeacher;
    }

    public int getGradeMark() {
        return gradeMark;
    }

    public static void resetCounter() {
        currentNumber = 0;
    }
}
