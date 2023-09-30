package Entities;


import Staff.UserRole;
import static Staff.UserRole.*;

public class User {
    private UserRole role;
    private String specialty;
    private String name;
    private String login;

    public boolean setRole(int isStudent, int isAdmin) {
        if(isStudent == 0 && isAdmin == 0 || isStudent == 1 && isAdmin == 1) {
            return false;
        }

        if(isStudent == 1 ) {
            role = STUDENT;
        } else if(isAdmin == 1) {
            role = ADMIN;
        }
        return true;
    }

    public UserRole getRole() {
        return role;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setSpecialty(String specialty) {
        this.specialty = specialty;
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
