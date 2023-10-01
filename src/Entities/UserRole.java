package Entities;

public enum UserRole {

    STUDENT("students"),
    ADMIN("admins");

    private final String tableName;

    UserRole(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
