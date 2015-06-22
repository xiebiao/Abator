package com.github.abator.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.abator.Abator;
import com.github.abator.ConfigKeys;
import com.github.abator.utils.NameUtils;

/**
 * 生成DAO默认实现
 * 
 * @author xiebiao[谢彪]
 */
public class DaoImplClassBuilder extends DaoClassBuilder {

  private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());
  private String interfaces;

  public DaoImplClassBuilder(String name) {
    super(name);
  }

  public void setInterfaces(String interfaces) {
    this.interfaces = interfaces;
  }

  protected void buildField() {
    sb.append("\n");
    sb.append(tab + tab + "public final String NAME_SPACE = this.getClass().getName();");
  }

  protected void buildImport() {
    if (sb.length() != 0) {
      sb.append("\n");
    }
    sb.append("import " + config.getProperty(ConfigKeys.DAO_PACKAGE) + "." + this.interfaces
        + ";\n");
    sb.append("import " + config.getProperty(ConfigKeys.DAO_IMPL_IMPORT) + ";\n");
    sb.append("\n");
  }

  protected void buildPackage() {
    String _package = this.config.getProperty(ConfigKeys.DAO_IMPL_PACKAGE);
    if (_package != null && !_package.equals("")) {
      sb.append("package " + _package + ";");
      sb.append("\n");
    }
  }

  protected void buildToString() {
    sb.append("\n");
    sb.append(tab + tab + "public String getNameSpace() {\n");
    sb.append(tab + tab + tab + tab + "return NAME_SPACE;\n");
    sb.append(tab + tab + "}\n");
  }

  protected void buildClassName() throws Exception {
    sb.append("\n");
    String _extends = config.getProperty(ConfigKeys.DAO_IMPL_EXTENDS);
    String suffix = config.getProperty(ConfigKeys.DAO_IMPL_SUFFIX);
    sb.append("public class " + NameUtils.getDomainName(this.name + suffix));
    sb.append(" extends " + _extends + " implements " + interfaces);
    sb.append(" {\n");
  }

  protected void buildAnnotate() {
    sb.append("\n");
    sb.append("/**\n");
    sb.append(" *  " + Abator.getSignature() + " \n");
    sb.append(" */");
    sb.append("\n");
  }

  public String build() throws Exception {
    if (sb == null) {
      throw new java.lang.InstantiationError();
    }
    doBuild();
    String _package = this.config.getProperty(ConfigKeys.DAO_IMPL_PACKAGE);
    String dirPath = _package == null ? "" : _package;
    if (_package != null && !_package.equals("")) {
      dirPath = dirPath.replace(".", File.separator);
      this.checkDirectory(dirPath);
    }
    String modelClassName =
        NameUtils.getDomainName(this.name) + this.config.getProperty(ConfigKeys.DAO_IMPL_SUFFIX);
    String fileName =
        this.config.getOutput() + File.separator + dirPath + File.separator + modelClassName
            + JAVA_FILE_SUFFIX;
    File newFile = new File(fileName);
    try {
      if (!newFile.exists()) {
        newFile.createNewFile();
      } else {
        newFile.delete();
      }
      FileOutputStream out = new FileOutputStream(newFile);
      out.write(sb.toString().getBytes());
      out.flush();
      out.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    String full_name = _package + "." + modelClassName;
    LOG.debug(fileName + " ... build success!");
    return full_name;
  }

}
