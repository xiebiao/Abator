package com.github.xiebiao.abator.database;

import java.util.HashSet;
import java.util.Set;

import com.github.xiebiao.abator.utils.NameUtils;

/**
 * @author xiebiao
 */
public class Table {

  private String name;
  private Set<Column> columns;
  private String comment;
  private String priKey;
  /**
   * 表别名
   */
  private String alias;

  public Table(String tableName) throws Exception {
    comment = "";
    columns = new HashSet<Column>();
    this.name = tableName;
    this.alias = NameUtils.getDomainName(tableName);
  }

  public void addColumn(Column column) {
    columns.add(column);
  }

  public String getName() {
    return name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    comment = comment == null ? "" : comment;
    this.comment = comment;
  }

  public Set<Column> getColumns() {
    return columns;
  }

  public void setColumns(Set<Column> columns) {
    this.columns = columns;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPriKey() {
    return priKey;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public void setPriKey(String key) {
    this.priKey = key;
  }

  /**
   * 生成数据库表SQL语句
   *
   * @return
   */
  public String toSql() {
    StringBuilder sb = new StringBuilder();
    sb.append("create table " + this.name + " \n");
    sb.append("(\n");
    Column[] cA = new Column[columns.size()];
    columns.toArray(cA);
    for (int i = 0; i < cA.length; i++) {
      Column c = cA[i];

      String l = c.getMaxLength() == 0L ? "" : "(" + cA[i].getMaxLength() + ")";
      sb.append(c.getName() + " " + c.getDataType() + l + " comment " + "'" + c.getComment()
          + "'\n");
      if (i < (cA.length - 1)) {
        sb.append(",\n");
      }
      if (c.isPrimaryKey()) {
        sb.append("primary key (" + c.getName() + "),\n");
      }
    }

    sb.append(");\n");
    return sb.toString();
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[").append("name=" + name).append(columns.toString()).append("]");
    return sb.toString();
  }
}
