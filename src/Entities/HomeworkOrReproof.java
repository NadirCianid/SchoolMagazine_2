package Entities;

public class HomeworkOrReproof {
    private static int currentNumber = 0; //счетчик количества текущих оценок

    public int getHomeworkOrReproofNumber() {
        return homeworkOrReproofNumber;
    }

    public String getHomeworkOrReproofDeadline() {
        return homeworkOrReproofDeadline;
    }

    public String getHomeworkOrReproofTeacher() {
        return homeworkOrReproofTeacher;
    }

    public String getHomeworkOrReproofBody() {
        return homeworkOrReproofBody;
    }

    private int homeworkOrReproofNumber;
    private String homeworkOrReproofDeadline;
    private String homeworkOrReproofTeacher;
    private String homeworkOrReproofBody;

    public HomeworkOrReproof(String homeworkOrReproofDeadline, String homeworkOrReproofTeacher, String homeworkOrReproofBody) {
        this.homeworkOrReproofNumber = ++currentNumber;
        this.homeworkOrReproofDeadline = homeworkOrReproofDeadline;
        this.homeworkOrReproofTeacher = homeworkOrReproofTeacher;
        this.homeworkOrReproofBody = homeworkOrReproofBody;
    }



    public static void resetCounter() {
        currentNumber = 0;
    }
}
