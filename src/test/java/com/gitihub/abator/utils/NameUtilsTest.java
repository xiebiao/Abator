package com.gitihub.abator.utils;

import junit.framework.Assert;

import org.junit.Test;

import com.github.abator.utils.NameUtils;

/**
 * @author <a href="mailto:joyrap@qq.com">joyrap@qq.com</a>
 * @date 6/22/15
 */
public class NameUtilsTest {
  @Test
  public void test_getCamelName() {
    Assert.assertEquals("aaBb", NameUtils.getCamelName("aa_bb"));
    Assert.assertEquals("aAAA", NameUtils.getCamelName("AAAA"));
  }
    @Test
    public void test_getDomainName() {
        Assert.assertEquals("AaBb", NameUtils.getDomainName("aa_bb"));
        Assert.assertEquals("AaBbCc", NameUtils.getDomainName("aa_bb_cc"));
        Assert.assertEquals("AaBbCcDDDDXX", NameUtils.getDomainName("aa_bb_cc_DDDD_XX"));
        Assert.assertEquals("CCCC", NameUtils.getDomainName("CCCC"));

    }
}
