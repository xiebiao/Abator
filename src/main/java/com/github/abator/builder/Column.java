package com.github.abator.builder;

public class Column {

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public int hashCode() {
    int hashCode = 0;
    char[] cc = this.name.toCharArray();
    for (char c : cc) {
      hashCode = hashCode + c;
    }
    return hashCode;

  }

  public boolean equals(Object obj) {
    if (obj instanceof Column) {
      Column c = (Column) obj;
      if (c.getName().equals(name)) {
        return true;
      }
    }
    return false;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("[").append("name=" + name)
        .append(",dataType=" + dataType + ",precision=" + precision + "]");
    return sb.toString();
  }

  public String getDataType() {
    return dataType;
  }

  public void setDataType(String dataType) {
    this.dataType = dataType;
  }

  public boolean isPrimaryKey() {
    return primaryKey;
  }

  public Long getMaxLength() {
    return maxLength;
  }

  public void setMaxLength(Long maxLength) {
    this.maxLength = maxLength;
  }

  public int getPrecision() {
    return precision;
  }

  public void setPrecision(int precision) {
    this.precision = precision;
  }

  public void setPrimaryKey(boolean primaryKey) {
    this.primaryKey = primaryKey;
  }

  public Column(String name) {

    this.name = name;
    comment = "";
    primaryKey = false;
  }

  private String name;
  private String dataType;
  private boolean primaryKey;
  private String comment;
  private Long maxLength = 0L;
  private int precision;
}
