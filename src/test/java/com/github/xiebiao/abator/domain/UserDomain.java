package com.github.xiebiao.abator.domain;


import com.github.xiebiao.dao.BaseDomain;

/**
 * 
 * Generate by : http://github.com/xiebiao/Abator
 */

public class UserDomain extends BaseDomain {

  private static final long serialVersionUID = 1L;
  // primary key
  protected int id;
  protected String name;

  public UserDomain() {}


  public void setId(int id) {
    this.id = id;
  }

  /**
   * 主键
   */
  public int getId() {
    return this.id;
  }

  public void setName(String name) {
    this.name = name;
  }

  /**
   * 用户名
   */
  public String getName() {
    return this.name;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("id:" + this.id + " ");
    sb.append("name:" + this.name + " ");
    return sb.toString();
  }
}
