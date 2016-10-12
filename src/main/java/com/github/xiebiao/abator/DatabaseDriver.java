package com.github.xiebiao.abator;

/**
 * @author xiebiao
 */
public enum DatabaseDriver {

    MYSQL("com.mysql.jdbc.Driver"),
    HSQLDB("org.hsqldb.jdbc.JDBCDriver"),
    ORACLE("oracle.jdbc.driver.OracleDriver"),
    MSSQL("com.microsoft.sqlserver.jdbc.SQLServerDriver"),
    UNKNOWN("");

    private String driver;

    DatabaseDriver(String driver) {
        this.driver = driver;
    }

    public String getDriver() {
        return this.driver;
    }

    public static DatabaseDriver value(String name) {
        if (name.equalsIgnoreCase(MYSQL.name())) {
            return MYSQL;
        } else if (name.equalsIgnoreCase(HSQLDB.name())) {
            return HSQLDB;
        } else if (name.equalsIgnoreCase(ORACLE.name())) {
            return ORACLE;
        } else if (name.equalsIgnoreCase(MSSQL.name())) {
            return MSSQL;
        }
        throw new RuntimeException("未知数据库类型");
    }
}
