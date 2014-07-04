package com.github.abator.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.abator.ConfigKeys;
import com.github.abator.utils.Utils;

/**
 * 生成DAO接口
 * @author xiebiao[谢彪]
 */
public class DaoClassBuilder extends ClassBuilder {

    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());

    public DaoClassBuilder(String name) {
        super(name);
    }

    /**
     * @return 返回类的全名
     */
    public String build() throws Exception {
        if (sb == null) {
            throw new java.lang.InstantiationError();
        }
        try {
            doBuild();
        } catch (Exception e1) {
            throw new Exception(e1);
        }
        String _package = this.config.getProperty(ConfigKeys.DAO_PACKAGE);
        String dirPath = _package == null ? "" : _package;
        if (_package != null && !_package.equals("")) {
            dirPath = dirPath.replace(".", File.separator);
            this.checkDirectory(dirPath);
        }
        String modelClassName = Utils.getDomainName(this.name) + this.config.getProperty(ConfigKeys.DAO_SUFFIX);
        String fileName = this.config.getOutput() + File.separator + dirPath + File.separator + modelClassName
                + JAVA_FILE_SUFFIX;
        File modelFile = new File(fileName);
        try {
            if (!modelFile.exists()) {
                modelFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(modelFile);
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // String full_name = _package + "." + modelClassName;
        LOG.debug(fileName + " ... build success!");
        return modelClassName;
    }

    protected void buildPackage() {
        String package_ = this.config.getProperty(ConfigKeys.DAO_PACKAGE);
        if (package_ != null && !package_.equals("")) {
            sb.append("package " + package_ + ";");
            sb.append("\n");
        }
    }

    protected void buildClassName()throws Exception {
        sb.append("\n");
        String _suffix = config.getProperty(ConfigKeys.DAO_SUFFIX);
        String _extends = config.getProperty(ConfigKeys.DAO_EXTENDS);
        sb.append("public interface " + Utils.getDomainName(this.name + _suffix));
        if (_extends != null) {
            sb.append(" extends " + _extends);
        }
        sb.append(" {\n");
    }

    protected void buildToString() {

    }

    @Override
    protected void buildImport() {

    }

    protected void buildAnnotate() {
        sb.append("\n");
        sb.append("/**\n");
        sb.append(" *  " + Utils.getSignature() + " \n");
        sb.append(" */");
        sb.append("\n");
    }

    @Override
    protected void buildStructure() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void buildField() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void buildSetterGetter() {
        // TODO Auto-generated method stub

    }

}
