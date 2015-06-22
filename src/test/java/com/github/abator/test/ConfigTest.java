package com.github.abator.test;

import junit.framework.TestCase;

import com.github.abator.builder.Config;

public class ConfigTest extends TestCase {

  private Config config;

  protected void setUp() throws Exception {
    config = Config.getInstance();
  }

  public void testGetTables() {
    System.out.print(config.getTables().size());

    org.junit.Assert.assertNotNull(config.getTables());
  }
}
