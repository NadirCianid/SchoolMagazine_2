package Entities;


import Staff.UserRole;
import static Staff.UserRole.*;

public class User {
    private UserRole role;
    private String name;
    private String login;

    public boolean setRole(int isStudent, int isTeacher) {
        if(isStudent == 0 && isTeacher == 0 || isStudent == 1 && isTeacher == 1) {
            return false;
        }

        if(isStudent == 1 ) {
            role = STUDENT;
        } else if(isTeacher == 1) {
            role = TEACHER;
        }
        return true;
    }

    public UserRole getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogin() {
        return login;
    }
}
