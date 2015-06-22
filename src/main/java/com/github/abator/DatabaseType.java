package com.github.abator;

public enum DatabaseType {
  MySQL("com.mysql.jdbc.Driver"), HSQLDB("org.hsqldb.jdbc.JDBCDriver"), ORACLE(
      "oracle.jdbc.driver.OracleDriver"), MSSQL("");

  private String driver;

  private DatabaseType(String driver) {
    this.driver = driver;
  }

  public String getDriver() {
    return this.driver;
  }

  public DatabaseType to(String name) {
    if (name.equalsIgnoreCase(MySQL.name())) {
      return MySQL;
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
