package com.github.xiebiao.abator.builder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.github.xiebiao.abator.Abator;
import com.github.xiebiao.abator.database.Column;
import org.apache.commons.lang3.StringUtils;

import com.github.xiebiao.abator.ConfigKeys;
import com.github.xiebiao.abator.database.Table;
import com.github.xiebiao.abator.utils.NameUtils;

/**
 * 生成mybatis-3的mapper文件
 * 
 * @author xiebiao[谢彪]
 */
public class MapperBuilder {

  private final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(this.getClass());
  private StringBuffer sb;
  private Table table;
  private String tab = "    ";
  private String namespace;
  private String domain;
  private Config config;

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
    sb.append("<!-- ").append(Abator.getSignature()).append(" -->\n");
    return sb.toString();
  }

  /**
   * @return
   */
  public String buildTableName() {
    sb.append(tab);
    sb.append("<sql id=\"table_name\">").append(table.getName()).append("</sql>");
    sb.append("\n");
    this.buildPaging();
    return sb.toString();
  }

  /**
   * <resultMap id="XXXDomainResultMap" type="XXX">
   */
  private void buildDefaultResultMap() throws Exception {
    sb.append(tab);
    sb.append("<resultMap  id=\"").append("ResultMap\"  type=\"").append(domain).append("\">\n");
    for (Column column : table.getColumns()) {

      if (JdbcType2JavaType.dataTypeMap.get(column.getDataType()).equals("byte[]")) {
        sb.append(tab).append(tab).append(tab).append("<result property=\"")
            .append(NameUtils.getCamelName(column.getName())).append("\"  column= \"")
            .append(column.getName())
            .append("\" typeHandler=\"org.apache.ibatis.type.ByteArrayTypeHandler\"/>\n");
      } else {
        sb.append(tab).append(tab).append(tab).append("<result property=\"")
            .append(NameUtils.getCamelName(column.getName())).append("\"  column= \"")
            .append(column.getName()).append("\"/>\n");
      }
    }
    sb.append(tab).append("</resultMap>\n");
  }

  /**
     * 
     */
  private void buildWhereCondition() throws Exception {
    sb.append(tab).append(tab).append("<where>\n");
    for (Column column : table.getColumns()) {
      if (JdbcType2JavaType.dataTypeMap.get(column.getDataType()).equals("int")) {
        sb.append(tab).append(tab).append(tab).append("<if test=\"")
            .append(NameUtils.getCamelName(column.getName())).append("  != null  and  ")
            .append(NameUtils.getCamelName(column.getName())).append(" != 0 \">\n");
      } else {
        sb.append(tab).append(tab).append(tab).append("<if test=\"")
            .append(NameUtils.getCamelName(column.getName())).append(" != null\">\n");
      }
      sb.append(tab).append(tab).append(tab).append(tab).append("  and ").append(column.getName())
          .append("=#{").append(NameUtils.getCamelName(column.getName())).append("}\n");
      sb.append(tab).append(tab).append(tab).append("</if>\n");
    }
    sb.append(tab).append(tab).append("</where>\n");
  }

  private void buildSetCondition() throws Exception {
    sb.append(tab).append(tab).append("<set>\n");
    for (Column column : table.getColumns()) {
      if (column.isPrimaryKey())
        continue;
      if (JdbcType2JavaType.dataTypeMap.get(column.getDataType()).equals("int")) {
        sb.append(tab).append(tab).append(tab).append("<if test=\"")
            .append(NameUtils.getCamelName(column.getName())).append("  != null  and  ")
            .append(NameUtils.getCamelName(column.getName())).append(" != 0 \">\n");
      } else {
        sb.append(tab).append(tab).append(tab).append("<if test=\"")
            .append(NameUtils.getCamelName(column.getName())).append(" != null\">\n");
      }
      sb.append(tab).append(tab).append(tab).append(tab).append("  ").append(column.getName())
          .append("=#{").append(NameUtils.getCamelName(column.getName())).append("},\n");
      sb.append(tab).append(tab).append(tab).append("</if>\n");
    }
    sb.append(tab).append(tab).append("</set>\n");
  }

  private void buildCondition() throws Exception {
    sb.append(tab);
    sb.append("<sql id=\"condition\">\n");
    buildWhereCondition();
    sb.append(tab).append("</sql>\n");
  }

  private void buildCount() {
    sb.append(tab).append("<select id=\"count\" resultType=\"int\" parameterType=\"")
        .append(domain).append("\">\n");
    sb.append(tab).append(tab).append("SELECT count(*) as value FROM \n");
    sb.append(tab).append(tab).append("<include refid=\"table_name\" />\n");
    sb.append(tab).append(tab).append("<include refid=\"condition\" />\n");
    sb.append(tab).append("</select>\n");
  }

  private void buildInsert(String tmp) throws Exception {
    sb.append(tab).append("<insert id=\"insert\">\n");
    sb.append(tab).append(tab).append("INSERT INTO \n");
    sb.append(tab).append(tab).append("<include refid=\"table_name\" />\n");
    sb.append(tab).append(tab).append("( \n");
    tmp = "";
    for (Column column : table.getColumns()) {
      tmp = tmp + tab + tab;
      tmp = tmp + tab + column.getName() + ",\n";
    }
    tmp = tmp.substring(0, tmp.length() - 2);
    sb.append(tmp).append("\n");

    sb.append(tab).append(tab).append(") \n");
    sb.append(tab).append(tab).append("VALUES \n");
    sb.append(tab).append(tab).append("( \n");

    tmp = "";
    for (Column column : table.getColumns()) {
      tmp = tmp + tab + tab;
      tmp = tmp + tab + "#{" + NameUtils.getCamelName(column.getName()) + "},\n";
    }
    tmp = tmp.substring(0, tmp.length() - 2);
    sb.append(tmp).append("\n");
    sb.append(tab).append(tab).append(") \n");
    sb.append(tab).append("</insert> \n");
  }

  private void buildFind() {

    sb.append(tab).append("<select id=\"find\" parameterType=\"string\" resultMap=\"")
        .append("ResultMap\">\n");
    sb.append(tab).append(tab).append("SELECT * FROM \n");
    sb.append(tab).append(tab).append("<include refid=\"table_name\" />\n");
    sb.append(tab).append(tab).append("WHERE ").append(table.getPriKey()).append(" = #{")
        .append(table.getPriKey()).append("} \n");
    sb.append(tab).append("</select>\n");
  }

  private void buildUpdate(String tmp) throws Exception {
    sb.append(tab).append("<update id=\"update\">\n");
    sb.append(tab).append(tab).append("UPDATE \n");
    sb.append(tab).append(tab).append("<include refid=\"table_name\" />\n");
    buildSetCondition();
    sb.append(tab).append(tab).append(tab).append("WHERE ").append(table.getPriKey()).append("=#{")
        .append(table.getPriKey()).append("} \n");
    sb.append(tab).append("</update> \n");
  }

  private void buildDelete(String tmp) {
    sb.append(tab).append("<delete id=\"delete\">\n");
    sb.append(tab).append(tab).append("DELETE FROM \n");
    sb.append(tab).append(tab).append("<include refid=\"table_name\" />\n");
    sb.append(tab).append(tab).append(" WHERE ").append(table.getPriKey()).append("=#{")
        .append(table.getPriKey()).append("} \n");
    sb.append(tab).append("</delete> \n");
  }

  private String buildOrderBy() {

    sb.append(tab).append(tab).append("<if test=\"orderByList != null and orderByList.size() > 0\">\n");
    sb.append(tab).append(tab).append("ORDER BY\n");
    sb.append(tab).append(tab).append(tab)
        .append("<foreach collection=\"orderByList\" item=\"item\" index=\"index\" separator=\",\">\n");
    sb.append(tab).append(tab).append(tab).append(tab).append("${item.name}  ${item.order}\n");
    sb.append(tab).append(tab).append(tab).append("</foreach>\n");
    sb.append(tab).append(tab).append("</if>\n");
    return sb.toString();
  }

  /**
   * 查询列表,带有分页参数
   */
  private void buildList() {
    sb.append(tab).append("<select id=\"list\" parameterType=\"").append(domain)
        .append("\" resultMap=\"").append("ResultMap\">\n");
    sb.append(tab).append(tab).append("SELECT * FROM \n");
    sb.append(tab).append(tab).append("<include refid=\"table_name\" />\n");
    sb.append(tab).append(tab).append("<include refid=\"condition\" />\n");
    buildOrderBy();
    sb.append(tab).append(tab).append("<include refid=\"paging\"/>\n");
    sb.append(tab).append("</select> \n");

  }

  /**
   * 生成表字段列表
   * 
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
   * 
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
   * 
   * @return
   */
  private String buildPaging() {
    sb.append(tab).append("<sql id=\"paging\">\n");
    sb.append(tab).append(tab).append("<if test=\"paging != null and paging == true \">\n");
    sb.append(tab).append(tab).append(tab).append("LIMIT #{offset},#{pageSize} \n");
    sb.append(tab).append(tab).append("</if>\n");
    sb.append(tab).append("</sql>\n");
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
    if (!dir.exists()) {
      dir.mkdirs();
    }
    String suffix = config.getProperty(ConfigKeys.SQL_MAPPER_SUFFIX);
    String fileName;
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
