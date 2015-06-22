package com.github.abator.builder;

import java.io.File;

/**
 * 
 * @author xiebiao
 */
public abstract class ClassBuilder {

  public static final String JAVA_FILE_SUFFIX = ".java";
  protected String tab;
  /** 类名 */
  protected String name;
  protected Config config;
  protected StringBuffer sb;

  public ClassBuilder(String name) {
    sb = new StringBuffer();
    this.name = name;
    tab = "    ";
  }

  public void setConfig(Config config) {
    this.config = config;
  }

  protected void checkDirectory(String dir) {
    String dirPath = this.config.getOutput() + File.separator + dir;
    File f = new File(dirPath);
    if (!f.exists()) {
      f.mkdirs();
    }
  }

  protected abstract void buildPackage();

  protected abstract void buildImport();

  protected abstract void buildAnnotate();

  protected abstract void buildStructure() throws Exception;

  protected abstract void buildClassName() throws Exception;

  protected abstract void buildField() throws Exception;

  protected abstract void buildSetterGetter() throws Exception;

  protected abstract void buildToString();

  /**
   * 执行该方法生成Java类
   * 
   * @return 生成类的名称
   */
  protected abstract String build() throws Exception;

  public final void buildClassEnd() {
    sb.append("\n}");
  }

  public final void doBuild() throws Exception {
    buildPackage();
    buildImport();
    buildAnnotate();
    buildClassName();
    buildField();
    buildStructure();
    buildSetterGetter();
    buildToString();
    buildClassEnd();
  }
}
