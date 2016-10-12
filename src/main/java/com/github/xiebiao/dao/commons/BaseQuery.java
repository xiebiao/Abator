package com.github.xiebiao.dao.commons;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author xiebiao
 */
public class BaseQuery implements Query {

  // private LinkedHashSet<FieldOrder> fieldOrders = new LinkedHashSet<FieldOrder>();
  private LinkedHashSet<OrderBy> orderByList;
  private Map<String, Object> parameters;
  private int pageSize;
  private int offset;
  private boolean isDistinct;
  private boolean isPaging;

  public BaseQuery() {
    orderByList = new LinkedHashSet();
  }

  @Override
  public void setOffset(int offset) {
    this.offset = offset;
  }

  @Override
  public int getOffset() {
    return offset;
  }

  @Override
  public void setDistinct(boolean distinct) {
    this.isDistinct = distinct;
  }

  @Override
  public boolean isDistinct() {
    return isDistinct;
  }

  @Override
  public boolean isPaging() {
    return this.isPaging;
  }

  @Override
  public void setPaging(boolean paging) {
    this.isPaging = paging;
  }

  @Override
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }

  @Override
  public int getPageSize() {
    return pageSize;
  }

  @Override
  public void setOrder(FieldOrder fieldOrder) {
    //
  }

  @Override
  public LinkedHashSet<OrderBy> getOrders() {
    return this.orderByList;
  }

  @Override
  public Query addOrderBy(OrderBy orderBy) {
    orderByList.add(orderBy);
    return this;
  }

  @Override
  public void setQueryParameters(Map<String, Object> parameters) {
    this.parameters = parameters;
  }

  @Override
  public Map<String, Object> getQueryParameters() {
    return parameters;
  }
}
