package com.github.xiebiao.abator.test;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class BaseTestCase extends TestCase {
  protected SqlSession session;

  public BaseTestCase() {
    try {
      String resource = "mybatis/SqlMapConfig.xml";
      InputStream inputStream = Resources.getResourceAsStream(resource);
      SqlSessionFactory sf = new SqlSessionFactoryBuilder().build(inputStream);
      session = sf.openSession();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
