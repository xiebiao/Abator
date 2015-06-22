package org.mybatis.generator.test;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.junit.Test;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.InvalidConfigurationException;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.extension.AbatorProgressCallback;
import org.mybatis.generator.internal.DefaultShellCallback;

/**
 * Mybatis generator 测试
 *
 * @author xiebiao[谢彪]
 */
public class MybatisGeneratorTest extends TestCase {

  @Test
  public void test() throws IOException, XMLParserException, SQLException, InterruptedException,
      InvalidConfigurationException {
    InputStream inputStream =
        Thread.currentThread().getContextClassLoader().getResourceAsStream("mybatis_generator.xml");
    List<String> warnings = new ArrayList<String>();
    boolean overwrite = true;
    ConfigurationParser cp = new ConfigurationParser(warnings);
    Configuration config = cp.parseConfiguration(inputStream);
    DefaultShellCallback callback = new DefaultShellCallback(overwrite);
    MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
    AbatorProgressCallback abatorProgressCallback = new AbatorProgressCallback();
    myBatisGenerator.generate(null);
  }

}
