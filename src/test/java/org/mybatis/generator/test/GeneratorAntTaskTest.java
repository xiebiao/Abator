package org.mybatis.generator.test;

import junit.framework.TestCase;

import org.junit.Test;
import org.mybatis.generator.ant.GeneratorAntTask;

import java.io.IOException;

/**
 * @author xiebiao
 */
public class GeneratorAntTaskTest extends TestCase {
    @Test
    public void test_execute() throws IOException {
        GeneratorAntTask generatorAntTask = new GeneratorAntTask();
        // InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(
        // "mybatis_generator.xml");
        String file =
                GeneratorAntTaskTest.class.getClassLoader().getResource("").getPath()
                        + "mybatis_generator.xml";
        generatorAntTask.setConfigfile(file);
        generatorAntTask.execute();
    }
}
