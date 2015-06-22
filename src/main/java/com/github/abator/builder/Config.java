package com.github.abator.builder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.*;

import org.apache.commons.lang3.StringUtils;

import com.github.abator.ConfigKeys;
import com.github.abator.DatabaseType;
import com.github.abator.database.Column;
import com.github.abator.database.Table;

/**
 * @author xiebiao
 */
public final class Config extends Properties {

  private static final long serialVersionUID = -6815576977124926101L;
  private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(Config.class);
  public static final String INFORMATION_SCHEMA = "information_schema";

  private static final String CONFIG_FILE = "abator.properties";
  private static final String COLUMNS_SQL =
      "SELECT * FROM COLUMNS where TABLE_SCHEMA=? AND TABLE_NAME=?";
  private String defaultOutput;
  // private String defaultBaseDao = "com.github.mybatis.dao.BaseDao";
  // private String defaultDaoInterface = "com.github.mybatis.dao.IDao";
  private static Config config;

  private Config() throws IOException {
    InputStream in =
        Thread.currentThread().getContextClassLoader().getResourceAsStream(CONFIG_FILE);
    init(in);
  }

  private Config(String configFile) throws IOException {
    init(new FileInputStream(new File(configFile)));
  }

  public void setDomainPackage(String _package) {
    this.put(ConfigKeys.DOMAIN_PACKAGE, _package);
  }

  public void setDaoPackage(String _package) {
    this.put(ConfigKeys.DAO_PACKAGE, _package);
  }

  public void setDaoImplPackage(String _package) {
    this.put(ConfigKeys.DAO_IMPL_PACKAGE, _package);
  }

  public static synchronized Config getInstance() throws IOException {
    if (config == null) {
      config = new Config();
    }
    String c = config.toString();
    LOG.info("#-----------------------------------------");
    for (Object key : config.keySet()) {
      LOG.info(key + " = " + config.get(key));
    }
    LOG.info("#-----------------------------------------");
    return config;
  }

  private void init(InputStream input) throws IOException {
    defaultOutput = System.getProperty("user.dir") + File.separator + "output";
    this.load(input);
    try {
      defaultOutput =
          StringUtils.isEmpty(this.getProperty(ConfigKeys.OUTPUT)) ? defaultOutput : this
              .getProperty(ConfigKeys.OUTPUT);
      this.put(ConfigKeys.OUTPUT, defaultOutput);
      String _daoSuffix = this.getProperty(ConfigKeys.DAO_SUFFIX);
      if (_daoSuffix == null) {
        this.put(ConfigKeys.DAO_SUFFIX, "Dao");
      }
      String _domainSuffix = this.getProperty(ConfigKeys.DOMAIN_SUFFIX);
      if (_domainSuffix == null) {
        this.put(ConfigKeys.DOMAIN_SUFFIX, "");
      }
      String _daoImplSuffix = this.getProperty(ConfigKeys.DAO_IMPL_SUFFIX);
      if (_daoImplSuffix == null) {
        this.put(ConfigKeys.DAO_IMPL_SUFFIX, "Impl");
      }
      Class.forName(DatabaseType.MySQL.getDriver());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  public String getOutput() {
    return this.getProperty(ConfigKeys.OUTPUT);
  }

  private Connection getConnection() {
    try {
      Connection connection =
          DriverManager.getConnection(this.getProperty(ConfigKeys.DB_URL),
              this.getProperty(ConfigKeys.DB_USER), this.getProperty(ConfigKeys.DB_PASSWORD));
      if (connection == null) {
        LOG.error("Can't get connection");
      }
      return connection;
    } catch (SQLException e) {
      e.printStackTrace();
      LOG.error("", e);
    }
    return null;
  }

  private void close(Connection connection) {
    try {
      connection.close();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Table> getTables() {
    Connection connection = this.getConnection();
    String sql = "SELECT * FROM TABLES WHERE TABLE_SCHEMA=?";
    List<Table> tables = null;
    String tablesStr = config.getProperty(ConfigKeys.HANDLE_TABLES);
    String[] tablesArr = StringUtils.split(tablesStr, ",");
    Set<String> configTables = new HashSet<String>();
    Collections.addAll(configTables, tablesArr);
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(sql);
      preparedStatement.setString(1, this.getProperty(ConfigKeys.DB_NAME));
      ResultSet rs = preparedStatement.executeQuery();
      tables = new ArrayList<Table>();
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME").trim();
        if (!configTables.contains(tableName)) {
          continue;
        }
        Table table;
        try {
          table = new Table(tableName);
        } catch (Exception e) {
          LOG.error(e.getMessage());
          continue;
        }
        table.setComment(rs.getString("TABLE_COMMENT").trim());
        table.setColumns(getColumns(table));
        table.setPriKey(getPrimaryKey(table));
        tables.add(table);
      }
      close(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return tables;
  }

  private String getPrimaryKey(Table table) {
    Connection connection = this.getConnection();
    String key = "";
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(COLUMNS_SQL);
      preparedStatement.setString(1, this.getProperty(ConfigKeys.DB_NAME));
      preparedStatement.setString(2, table.getName());
      ResultSet rs = preparedStatement.executeQuery();
      while (rs.next()) {
        if (rs.getString("COLUMN_KEY").equalsIgnoreCase("PRI")) {
          key = rs.getString("COLUMN_NAME").trim();
        }
      }
      close(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return key;
  }

  public Set<Column> getColumns(Table table) {
    Connection connection = this.getConnection();
    Set<Column> columns = null;
    try {
      PreparedStatement preparedStatement = connection.prepareStatement(COLUMNS_SQL);
      preparedStatement.setString(1, this.getProperty(ConfigKeys.DB_NAME));
      preparedStatement.setString(2, table.getName());
      ResultSet rs = preparedStatement.executeQuery();
      columns = new HashSet<Column>();
      boolean hasPriKey = false;
      while (rs.next()) {
        Column column = new Column(rs.getString("COLUMN_NAME").trim());
        column.setDataType(rs.getString("DATA_TYPE"));
        column.setPrecision(rs.getInt("NUMERIC_PRECISION"));
        column.setMaxLength(rs.getLong("CHARACTER_MAXIMUM_LENGTH"));
        if (rs.getString("COLUMN_KEY").equalsIgnoreCase("PRI")) {
          column.setPrimaryKey(true);
          hasPriKey = true;
        }
        column.setComment(rs.getString("COLUMN_COMMENT").trim());
        columns.add(column);
      }
      if (!hasPriKey) {
        LOG.warn("table(" + table.getName() + ") must have PRIMARY KEY. ");
      }
      close(connection);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return columns;
  }

  public Properties getProperties() {
    return this;
  }

}
