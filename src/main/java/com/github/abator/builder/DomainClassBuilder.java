package com.github.abator.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.github.abator.ConfigKeys;
import com.github.abator.utils.Utils;

/**
 * 生成表对应的domain
 * @author xiebiao[谢彪]
 */
public class DomainClassBuilder extends ClassBuilder {

    private static final org.slf4j.Logger LOG    = org.slf4j.LoggerFactory.getLogger(DomainClassBuilder.class);

    private List<String>                  fields = new ArrayList<String>();
    private Table                         table;

    public DomainClassBuilder(Table table) {
        super(table.getName());
        this.table = table;
    }

    public String build() throws Exception {
        if (sb == null) {
            throw new java.lang.InstantiationError();
        }
        doBuild();
        String _package = this.config.getProperty(ConfigKeys.DOMAIN_PACKAGE);
        String dirPath = _package == null ? "" : _package;
        if (_package != null && !_package.equals("")) {
            dirPath = dirPath.replace(".", File.separator);
            this.checkDirectory(dirPath);
        }
        String modelClassName = Utils.getDomainName(table.getName())
                + this.config.getProperty(ConfigKeys.DOMAIN_SUFFIX);
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
            LOG.debug(fileName + " .. build success!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return this.config.getProperty(ConfigKeys.DOMAIN_PACKAGE) + "." + modelClassName;
    }

    public void buildStructure() throws Exception {
        sb.append("\n");
        sb.append(tab + "public "
                + Utils.getDomainName(table.getName() + this.config.getProperty(ConfigKeys.DOMAIN_SUFFIX)) + "() {\n");
        sb.append(tab + "}\n");
    }

    public DomainClassBuilder from(Table table) {
        sb = new StringBuffer();
        if (table == null || table.getName().equals("")) {
            throw new java.lang.IllegalArgumentException();
        }
        this.table = table;
        return this;
    }

    protected void buildPackage() {
        String _package = this.config.getProperty(ConfigKeys.DOMAIN_PACKAGE);
        if (_package != null && !_package.equals("")) {
            sb.append("package " + _package + ";");
            sb.append("\n");
        }
    }

    protected void buildImport() {
        if (sb.length() != 0) {
            sb.append("\n");
        }
        if (table.getColumns() == null) {
            return;
        } else {
            for (Column c : table.getColumns()) {
                String dataType = DataType2Java.dataTypeMap.get(c.getDataType());
                if (dataType != null && dataType.equals("Date")) {
                    sb.append("import java.util.Date;\n");
                    break;
                }
            }
        }
        sb.append("\n");
    }

    protected void buildAnnotate() {
        sb.append("\n");
        sb.append("/**\n");
        sb.append(" *  " + table.getComment() + "\n");
        sb.append(" *  " + Utils.getSignature() + " \n");
        sb.append(" */");
        sb.append("\n");
    }

    protected void buildClassName() throws Exception {
        sb.append("\n");
        String _extends = config.getProperty(ConfigKeys.DOMAIN_EXTENDS);
        String _suffix = config.getProperty(ConfigKeys.DOMAIN_SUFFIX);
        sb.append("public class " + Utils.getDomainName(table.getName() + _suffix));
        if (_extends != null) {
            sb.append(" extends " + _extends);
        }
        sb.append(" {\n");
    }

    protected void buildField() throws Exception {
        sb.append("\n");
        sb.append(tab + "private static final long serialVersionUID = 1L;\n");
        Set<Column> columns = table.getColumns();
        if (columns == null) {
            LOG.error(" Table=" + table.getName() + " has no column.");
            return;
        } else {
            for (Column c : columns) {
                LOG.debug("Table name:" + this.table.getName());
                String name = Utils.getCamelName(Utils.getCamelName(c.getName()));
                String dataType = DataType2Java.dataTypeMap.get(c.getDataType());
                if (dataType == null) {
                    LOG.error("Column=" + c.getName() + " has null datatype.");
                    continue;
                }
                if (JavaKeyWord.isJavaKeyWord(name)) {
                    name = "_" + name;
                }

                sb.append(tab + "/**  " + c.getComment());
                if (c.isPrimaryKey()) {
                    sb.append(" (Primary Key)");
                }
                sb.append(" */\n");
                sb.append(tab + "protected " + getJavaDataType(c) + " " + name + ";\n");
                fields.add(name);
            }
        }

    }

    private String getJavaDataType(Column column) {
        if (column.getDataType().equals("bit") && column.getPrecision() == 1) {
            return "boolean";
        } else {
            return DataType2Java.dataTypeMap.get(column.getDataType());
        }
    }

    protected void buildSetterGetter() throws Exception {
        sb.append("\n");
        Set<Column> columns = table.getColumns();
        for (Column c : columns) {
            String field = Utils.getCamelName(c.getName());
            String _field = field;
            if (JavaKeyWord.isJavaKeyWord(field)) {
                _field = "_" + field;
            }
            sb.append("\n");
            sb.append(
                    tab + "public void set" + field.substring(0, 1).toUpperCase() + field.substring(1, field.length())
                            + "(" + getJavaDataType(c) + " " + _field + ")").append(" {\n").append(
                    tab + tab + "this." + _field + " = " + _field + ";\n").append(tab + "}\n");
            sb.append("\n");
            sb.append(
                    tab + "public " + getJavaDataType(c) + " get" + field.substring(0, 1).toUpperCase()
                            + field.substring(1, field.length()) + "()").append(" {\n").append(tab).append(
                    tab + "return this." + _field + ";\n").append(tab + "}\n");
        }
    }

    @Override
    protected void buildToString() {
        sb.append("\n");
        sb.append(tab + "public String toString(){\n");
        sb.append(tab + tab + "StringBuilder sb = new StringBuilder();\n");
        int size = fields.size();
        sb.append(tab + tab + "sb.append(\"{\");\n");
        for (int i = 0; i < size - 1; i++) {
            sb.append(tab + tab + "sb.append(\"\\\"" + fields.get(i) + "\\\":\\\"\"+this." + fields.get(i)
                    + "+\"\\\"\");\n");
            if (i < (size - 2)) {
                sb.append(tab + tab + "sb.append(\",\");\n");
            }
        }
        sb.append(tab + tab + "sb.append(\"}\");\n");
        sb.append(tab + tab + "return sb.toString();\n");
        sb.append(tab + "}");
        this.fields.clear();
    }
}
