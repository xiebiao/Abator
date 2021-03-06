package com.github.xiebiao.abator.utils;

public class NameUtils {

  private static final org.slf4j.Logger LOG = org.slf4j.LoggerFactory.getLogger(NameUtils.class);

  /**
   * Domain类名称
   * <p>
   * 首字母大写
   * </p>
   * 
   * @param input
   * @return Domain类名称
   */
  public static String getDomainName(String input) {
    if (input.contains("-") && input.contains("_")) {
      input.replaceAll("-", "_");
    }
    String[] t = input.split("_");
    StringBuffer modelClassName = new StringBuffer();
    for (String s : t) {
      modelClassName.append(s.substring(0, 1).toUpperCase()).append(s.substring(1, s.length()));
    }
    return modelClassName.toString();
  }

  public static String getCamelName(String name) {
    name = getDomainName(name);
    name = name.substring(0, 1).toLowerCase() + name.substring(1, name.length());
    return name;
  }


}
