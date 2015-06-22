package com.github.abator.dao;

import org.junit.Test;

import com.github.abator.test.BaseTestCase;

public class UserDaoTest extends BaseTestCase {
  @Test
  public void test_count() {

    UserDao userDao = this.session.getMapper(UserDao.class);
    userDao.find("1");
    // Assert.assertEquals("com.github.abator.dao.impl.UserDaoImpl",UserDaoImpl.class.getName() );

  }
}
