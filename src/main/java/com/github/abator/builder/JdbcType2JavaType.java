package com.github.abator.builder;

import java.util.HashMap;
import java.util.Map;

/**
 * @see org.apache.ibatis.type.JdbcType
 */
public class JdbcType2JavaType {

  public static Map<String, String> dataTypeMap = new HashMap<String, String>();
  static {
    dataTypeMap.put("time", "Date");/* 以'HH:MM:SS'格式显示 */
    dataTypeMap.put("year", "Date");/* 以YYYY 格式显示 */
    dataTypeMap.put("timestamp", "Date");/* YYYY-MM-DD HH:MM:SS'格式 */
    dataTypeMap.put("date", "Date");/* 以'YYYY-MM-DD'格式显示DATE值 */
    dataTypeMap.put("datetime", "Date");/* 以'YYYY-MM-DD HH:MM:SS'格式 */
    dataTypeMap.put("double", "double");/*-1.7976931348623157E+308到-2.2250738585072014E-*/
    dataTypeMap.put("float", "float");/*-3.402823466E+38到-1.175494351E-38*/
    dataTypeMap.put("binary", "byte[]");
    dataTypeMap.put("varbinary", "byte[]");
    dataTypeMap.put("tinyblob", "byte[]");
    dataTypeMap.put("mediumint", "Integer");
    dataTypeMap.put("smallint", "Integer");
    dataTypeMap.put("tinyint", "Integer");/*-128到127*/
    dataTypeMap.put("smallint", "Integer");/*-32768到32767*/
    dataTypeMap.put("mediumint", "Integer");/*-8388608到8388607*/
    dataTypeMap.put("decimal", "Double");/* 压缩的“严格”定点数 */
    dataTypeMap.put("integer", "Integer");
    dataTypeMap.put("int", "Integer");/*-2147483648到2147483647*/
    dataTypeMap.put("bigint", "Integer");/*-9223372036854775808到9223372036854775807*/

    dataTypeMap.put("enum", "enum");
    dataTypeMap.put("bit", "String");
    dataTypeMap.put("char", "String");
    dataTypeMap.put("varchar", "String");
    dataTypeMap.put("text", "String");
    dataTypeMap.put("tinytext", "String");
    dataTypeMap.put("mediumtext", "String");
    dataTypeMap.put("longtext", "String");
  }
}
