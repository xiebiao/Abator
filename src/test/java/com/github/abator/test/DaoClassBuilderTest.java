package com.github.abator.test;

import java.util.HashSet;
import java.util.Set;

import com.github.abator.database.Column;
import com.github.abator.database.Table;
import org.junit.Test;

import com.github.abator.builder.Config;
import com.github.abator.builder.DaoClassBuilder;

public class DaoClassBuilderTest extends BaseTestCase {

  private DaoClassBuilder daoClassBuilder;
  private Config config;

  protected void setUp() throws Exception {
    // super.setUp();
    config = Config.getInstance();
    config.put("domain.package", "com.xiebiao.domain");
    config.put("dao.package", "com.xiebiao.dao");
    daoClassBuilder = new DaoClassBuilder("JustTestDao");
  }

  @Test
  public void test_build() {
    Column name = new Column("name");
    name.setDataType("varchar");

    Column year = new Column("data");
    year.setDataType("year");

    Column datetime = new Column("date");
    datetime.setDataType("datetime");

    Column _float = new Column("float");

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
      daoClassBuilder.build();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
