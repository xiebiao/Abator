package com.github.abator.dao;

import com.github.abator.test.BaseTestCase;
import org.junit.Test;

public class UserDaoTest extends BaseTestCase {
    @Test
    public void test_count() {

        UserDao userDao = this.session.getMapper(UserDao.class);
        userDao.find("1");
        // Assert.assertEquals("com.github.abator.dao.impl.UserDaoImpl",UserDaoImpl.class.getName() );

    }
}
