package com.github.abator.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.lang3.StringUtils;

import com.github.abator.ConfigKeys;
import com.github.abator.utils.Utils;

/**
 * 生成mybatis-3的mapper文件
 * 
 * <pre>
 *  sqlmapper中所有分页参数与 {@link  com.github.mybatis.domain.BaseDomain#getStart_()},
 *  {@link  com.github.mybatis.domain.BaseDomain#getRow_()}
 * </pre>
 * @author xiebiao[谢彪]
 */
public class MapperBuilder {

    private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());
    private StringBuffer           sb;
    private Table                  table;
    private String                 tab = "    ";
    private String                 namespace;
    private String                 domain;
    private Config                 config;

    public MapperBuilder(Config config) {
        this.config = config;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public MapperBuilder from(Table table) {
        this.sb = new StringBuffer();
        if (table == null || table.getName().equals("")) {
            throw new java.lang.IllegalArgumentException();
        }
        this.table = table;
        return this;
    }

    public String buildXmlHeader() {
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        sb.append("\n");
        return sb.toString();
    }

    public String buildDtd() {
        sb.append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN \"\n\"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">");
        sb.append("\n");
        sb.append("<!-- " + Utils.getSignature() + " -->\n");
        return sb.toString();
    }

    /**
     * @return
     */
    public String buildTableName() {
        sb.append(tab);
        sb.append("<sql id=\"table_name\">" + table.getName() + "</sql>");
        sb.append("\n");
        this.buildDataPage();
        return sb.toString();
    }

    /**
     * <resultMap id="XXXDomainResultMap" type="XXX">
     */
    private void buildDefaultResultMap() throws Exception {
        sb.append(tab);
        sb.append("<resultMap  id=\"" + domain + "ResultMap\"  type=\"" + domain + "\">\n");
        for (Column column : table.getColumns()) {
            System.out.println(column.getDataType());
            if (DataType2Java.dataTypeMap.get(column.getDataType()).equals("byte[]")) {
                sb.append(tab + tab + tab + "<result property=\"" + Utils.getCamelName(column.getName())
                        + "\"  column= \"" + column.getName()
                        + "\" typeHandler=\"org.apache.ibatis.type.ByteArrayTypeHandler\"/>\n");
            } else {
                sb.append(tab + tab + tab + "<result property=\"" + Utils.getCamelName(column.getName())
                        + "\"  column= \"" + column.getName() + "\"/>\n");
            }
        }
        sb.append(tab + "</resultMap>\n");
    }

    /**
     * 
     */
    private void buildWhereCondition() throws Exception {
        for (Column column : table.getColumns()) {
            if (DataType2Java.dataTypeMap.get(column.getDataType()).equals("int")) {
                sb.append(tab + tab + tab + "<if test=\"" + Utils.getCamelName(column.getName()) + "  != null  and  "
                        + Utils.getCamelName(column.getName()) + " != 0 \">\n");
            } else {
                sb.append(tab + tab + tab + "<if test=\"" + Utils.getCamelName(column.getName()) + " != null\">\n");
            }
            sb.append(tab + tab + tab + tab + "  and " + column.getName() + "=#{"
                    + Utils.getCamelName(column.getName()) + "}\n");
            sb.append(tab + tab + tab + "</if>\n");
        }
    }

    private void buildSetCondition() throws Exception {
        sb.append(tab + tab + tab + "<set>\n");
        for (Column column : table.getColumns()) {
            if (column.isPrimaryKey()) continue;
            if (DataType2Java.dataTypeMap.get(column.getDataType()).equals("int")) {
                sb.append(tab + tab + tab + "<if test=\"" + Utils.getCamelName(column.getName()) + "  != null  and  "
                        + Utils.getCamelName(column.getName()) + " != 0 \">\n");
            } else {
                sb.append(tab + tab + tab + "<if test=\"" + Utils.getCamelName(column.getName()) + " != null\">\n");
            }
            sb.append(tab + tab + tab + tab + "  " + column.getName() + "=#{" + Utils.getCamelName(column.getName())
                    + "},\n");
            sb.append(tab + tab + tab + "</if>\n");
        }
        sb.append(tab + tab + tab + "</set>\n");
    }

    private void buildCondition() throws Exception {
        sb.append(tab);
        sb.append("<sql id=\"condition\">\n");
        sb.append(tab + tab + "<where>\n");
        buildWhereCondition();
        sb.append(tab + tab + "</where>\n");
        sb.append(tab + "</sql>\n");
    }

    private void buildCount() {
        sb.append(tab + "<select id=\"count\" resultType=\"int\" parameterType=\"" + domain + "\">\n");
        sb.append(tab + tab + "SELECT count(*) as value FROM \n");
        sb.append(tab + tab + "<include refid=\"table_name\" />\n");
        sb.append(tab + tab + "<include refid=\"condition\" />\n");
        sb.append(tab + "</select>\n");
    }

    private void buildInsert(String tmp) throws Exception {
        sb.append(tab + "<insert id=\"insert\"> \n");
        sb.append(tab + tab + "INSERT INTO \n");
        sb.append(tab + tab + "<include refid=\"table_name\" /> \n");
        sb.append(tab + tab + "( \n");
        tmp = "";
        for (Column column : table.getColumns()) {
            tmp = tmp + tab + tab;
            tmp = tmp + tab + column.getName() + ",\n";
        }
        tmp = tmp.substring(0, tmp.length() - 2);
        sb.append(tmp + "\n");

        sb.append(tab + tab + ") \n");
        sb.append(tab + tab + "VALUES \n");
        sb.append(tab + tab + "( \n");

        tmp = "";
        for (Column column : table.getColumns()) {
            tmp = tmp + tab + tab;
            tmp = tmp + tab + "#{" + Utils.getCamelName(column.getName()) + "},\n";
        }
        tmp = tmp.substring(0, tmp.length() - 2);
        sb.append(tmp + "\n");
        sb.append(tab + tab + ") \n");
        sb.append(tab + "</insert> \n");
    }

    private void buildFind() {

        sb.append(tab + "<select id=\"find\" parameterType=\"string\" resultMap=\"" + domain + "ResultMap\">\n");
        sb.append(tab + tab + "SELECT * FROM \n");
        sb.append(tab + tab + "<include refid=\"table_name\" /> \n");
        sb.append(tab + tab + "WHERE " + table.getPriKey() + " = #{" + table.getPriKey() + "} \n");
        sb.append(tab + "</select>\n");
    }

    private void buildUpdate(String tmp) throws Exception {
        sb.append(tab + "<update id=\"update\"> \n");
        sb.append(tab + tab + "UPDATE \n");
        sb.append(tab + tab + "<include refid=\"table_name\" />\n");
        buildSetCondition();
        sb.append(tab + tab + tab + "WHERE " + table.getPriKey() + "=#{" + table.getPriKey() + "} \n");

        sb.append(tab + "</update> \n");
    }

    private void buildDelete(String tmp) {
        sb.append(tab + "<delete id=\"delete\"> \n");
        sb.append(tab + tab + "DELETE FROM \n");
        sb.append(tab + tab + "<include refid=\"table_name\" />\n");
        sb.append(tab + tab + " WHERE " + table.getPriKey() + "=#{" + table.getPriKey() + "} \n");

        sb.append(tab + "</delete> \n");
    }

    /**
     * 查询列表,带有分页参数
     */
    private void buildList() {
        sb.append(tab + "<select id=\"list\" parameterType=\"" + domain + "\" resultMap=\"" + domain
                + "ResultMap\"> \n");
        sb.append(tab + tab + "SELECT * FROM \n");
        sb.append(tab + tab + "<include refid=\"table_name\" /> \n");
        sb.append(tab + tab + "<include refid=\"condition\" /> \n");
        sb.append(tab + tab + "<if test=\"order_ != null\"> \n");
        sb.append(tab + tab + tab + "order by ${order_} \n");
        sb.append(tab + tab + "</if> \n");

        sb.append(tab + tab + "<if test=\"sort_ != null\"> \n");
        sb.append(tab + tab + tab + "${sort_} \n");
        sb.append(tab + tab + "</if> \n");

        sb.append(tab + tab + "<include refid=\"dataPage\"/>\n");

        sb.append(tab + "</select> \n");

    }

    /**
     * 生成表字段列表
     * @return
     */
    public String buildDefault() throws Exception {
        buildDefaultResultMap();
        sb.append(tab);
        String tmp = "<sql id=\"field_list\">\n";
        for (Column column : table.getColumns()) {
            tmp = tmp + tab + tab;
            tmp = tmp + column.getName() + ",\n";
        }
        tmp = tmp.substring(0, tmp.length() - 2);
        tmp = tmp + "\n" + tab + "</sql>\n";
        sb.append(tmp);
        buildCondition();
        buildCount();
        buildFind();
        buildUpdate(tmp);
        buildDelete(tmp);
        buildInsert(tmp);
        buildList();
        return sb.toString();
    }

    /**
     * 生成namespace
     * @return
     */
    public String buildBodyHeader() {
        sb.append("<mapper namespace=\"" + this.namespace + "\">");
        sb.append("\n");
        return sb.toString();
    }

    public String buildEnd() {
        sb.append("</mapper>");
        return sb.toString();
    }

    /**
     * 生成分页判断
     * @return
     */
    private String buildDataPage() {
        sb.append(tab + "<sql id=\"dataPage\"> \n");
        sb.append(tab + tab + "<if test=\"rows_ != 0\"> \n");
        sb.append(tab + tab + tab + "LIMIT #{start_},#{rows_} \n");
        sb.append(tab + tab + "</if>\n");
        sb.append(tab + "</sql>\n");
        return sb.toString();
    }

    public final void doBuild() throws Exception {
        buildXmlHeader();
        buildDtd();
        buildBodyHeader();
        buildTableName();
        buildDefault();
        buildEnd();
    }

    /**
     * @return 生成文件的绝对路径
     */
    public String build() throws Exception {
        doBuild();
        String dirPath = config.getOutput() + File.separator + "mapper";
        File dir = new File(dirPath);
        if (dir.exists() == false) {
            dir.mkdirs();
        }
        String suffix = config.getProperty(ConfigKeys.SQL_MAPPER_SUFFIX);
        String fileName = null;
        if (StringUtils.isEmpty(suffix)) {
            fileName = dirPath + File.separator + table.getName() + ".xml";
        } else {
            fileName = dirPath + File.separator + table.getName() + "_" + suffix + ".xml";
        }
        File modelFile = new File(fileName);
        try {
            if (!modelFile.exists()) {
                // modelFile.mkdirs();
                modelFile.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(modelFile);
            out.write(sb.toString().getBytes());
            out.flush();
            out.close();
            LOG.debug(fileName + " ... build success!");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName;
    }
}
