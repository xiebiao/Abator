package com.github.xiebiao.abator.test;

import java.util.HashSet;
import java.util.Set;

import com.github.xiebiao.abator.builder.Config;
import com.github.xiebiao.abator.builder.DaoImplClassBuilder;
import com.github.xiebiao.abator.database.Column;
import com.github.xiebiao.abator.database.Table;

public class DaoImplClassBuilderTest extends BaseTestCase {

  private DaoImplClassBuilder daoImplClassBuilder;
  private Config config;

  public void test_build() {
    try {
      config = Config.getInstance();
    } catch (Exception e) {
      e.printStackTrace();
    }
    config.getProperties().put("dao.impl.package", "com.xiebiao.dao.impl");
    daoImplClassBuilder = new DaoImplClassBuilder("JustTestDaoImpl");
    Column name = new Column("name");
    name.setName("name");
    name.setDataType("varchar");

    Column year = new Column("date");
    year.setName("date");
    year.setDataType("year");

    Column datetime = new Column("date");
    datetime.setName("date");
    datetime.setDataType("datetime");

    Column _float = new Column("float");
    _float.setName("float");
    _float.setDataType("float");
    _float.setComment("字段说明");

    Set<Column> columns = new HashSet<Column>();
    columns.add(name);
    columns.add(year);
    columns.add(datetime);
    columns.add(_float);
    try {
      Table table = new Table("Person");
      table.setColumns(columns);
      table.setComment("这是表说明");
      daoImplClassBuilder.build();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
