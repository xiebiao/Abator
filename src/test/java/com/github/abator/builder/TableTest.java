package com.github.abator.builder;

import java.util.HashSet;
import java.util.Set;

import com.github.abator.database.Column;
import com.github.abator.database.Table;
import junit.framework.TestCase;

public class TableTest extends TestCase {

  public void test() {
    Table t = null;
    try {
      t = new Table("user");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Set<Column> columns = new HashSet<Column>();

    Column name = new Column("name");
    name.setComment("名字");
    name.setDataType("varchar");
    name.setMaxLength(20L);
    columns.add(name);

    Column id = new Column("id");
    id.setComment("名字");
    id.setDataType("int");
    id.setPrimaryKey(true);
    id.setMaxLength(20L);

    columns.add(id);

    t.setColumns(columns);
    System.out.print(t.toSql());
  }
}
