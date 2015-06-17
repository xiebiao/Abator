package com.github.commons;

/**
 * @author bjxieb
 * @date 6/17/15
 */
public class OrderBy {
  public enum ORDER {
    ASC, DESC
  }

  private String column;
  private ORDER order;

  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  public ORDER getOrder() {
    return order;
  }

  public void setOrder(ORDER order) {
    this.order = order;
  }
}
