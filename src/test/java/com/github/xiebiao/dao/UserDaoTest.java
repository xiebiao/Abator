package com.github.xiebiao.dao;

import org.junit.Test;

import com.github.xiebiao.abator.test.BaseTestCase;

public class UserDaoTest extends BaseTestCase {
  @Test
  public void test_count() {

    UserDao userDao = this.session.getMapper(UserDao.class);
    userDao.find("1");
    // Assert.assertEquals("UserDaoImpl",UserDaoImpl.class.getName() );

  }
}
