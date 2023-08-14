package Staff;

public enum UserRole {

    STUDENT("students"),
    TEACHER("teachers");

    private final String tableName;

    UserRole(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }
}
