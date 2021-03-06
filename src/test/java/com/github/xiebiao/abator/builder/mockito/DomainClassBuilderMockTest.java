package com.github.xiebiao.abator.builder.mockito;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.github.xiebiao.abator.builder.Config;
import com.github.xiebiao.abator.builder.DomainClassBuilder;
import com.github.xiebiao.abator.database.Table;

public class DomainClassBuilderMockTest {

  private DomainClassBuilder dcb;

  @Before
  public void setUp() {

  }

  @Test
  public void test_() {
    try {
      Table table = new Table("test");
      dcb = new DomainClassBuilder(table);
      try {
        dcb.setConfig(Config.getInstance());
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      dcb.build();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
