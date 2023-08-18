package Entities;

public class SubjectFinalGrade {
    private static int currentNumber = 0; //счетчик количества текущих оценок
    private int number;
    private String subjectName;
    private float firstQuarter;
    private float secondQuarter;
    private float thirdQuarter;
    private float fourthQuarter;
    private float finalGrade;

    public SubjectFinalGrade(String subjectName, int quarterNumber, float quarterMark) {
        this.number = ++currentNumber;
        this.subjectName = subjectName;

        switch (quarterNumber) {
            case 1 -> firstQuarter = quarterMark;
            case 2 -> secondQuarter = quarterMark;
            case 3 -> thirdQuarter = quarterMark;
            case 4 -> fourthQuarter = quarterMark;
        }
    }

    public int getNumber() {
        return number;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public float getFirstQuarter() {
        return firstQuarter;
    }

    public float getSecondQuarter() {
        return secondQuarter;
    }

    public float getThirdQuarter() {
        return thirdQuarter;
    }

    public float getFourthQuarter() {
        return fourthQuarter;
    }

    public float getFinalGrade() {
        return finalGrade;
    }

    public void addQuarterMark(int quarterNumber, float quarterMark) {
        switch (quarterNumber) {
            case 1 -> firstQuarter = quarterMark;
            case 2 -> secondQuarter = quarterMark;
            case 3 -> thirdQuarter = quarterMark;
            case 4 -> fourthQuarter = quarterMark;
        }

        finalGrade = (firstQuarter + secondQuarter + thirdQuarter + fourthQuarter) / 4;
    }

    public static void resetCounter() {
        currentNumber = 0;
    }
}
