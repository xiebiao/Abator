package com.github.commons;

import junit.framework.Assert;
import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * @author xiebiao
 */
public class DataPageTest extends TestCase {

    @Before
    public void setUp() {

    }

    @Test
    public void test() {
        DataPage<String> dataPage = new DataPage<String>(100, 1, 20);

        int totalPage = dataPage.getTotalPage();
        Assert.assertEquals(5, totalPage);

    }

}
