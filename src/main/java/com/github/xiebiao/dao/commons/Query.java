package com.github.xiebiao.dao.commons;

import java.util.LinkedHashSet;
import java.util.Map;

/**
 * @author xiebiao
 */
public interface Query {

  void setOffset(int offset);

  int getOffset();

  /**
   * 设置是否排重
   * 
   * @param distinct
   */
  void setDistinct(boolean distinct);

  /**
   * 是否排重
   * 
   * @return
   */
  boolean isDistinct();

  /**
   * 是否分页
   * 
   * @return
   */
  boolean isPaging();

  /**
   * 设置是否分页
   * 
   * @param paging
   */
  void setPaging(boolean paging);

  void setPageSize(int pageSize);

  int getPageSize();

  /**
   * 排序字段内部实现使用<code>LinkedHashSet</code>，So,排序字段有先后顺序，例如：
   * 
   * <pre>
   * Query query = new Query();
   * query.setOrder(new FieldOrder(&quot;updateTime&quot;, FieldOrder.ORDER.ASC));
   * query.setOrder(new FieldOrder(&quot;addTime&quot;, FieldOrder.ORDER.DESC));
   * </pre>
   * 
   * 则会生成SQL:
   * 
   * <pre>
   *     ... ORDER BY updateTime ASC , addTime DESC ...
   * </pre>
   * 
   * @param fieldOrder
   */
  @Deprecated
  void setOrder(FieldOrder fieldOrder);

  LinkedHashSet<OrderBy> getOrders();

  Query addOrderBy(OrderBy orderBy);

  void setQueryParameters(Map<String, Object> parameters);

  Map<String, Object> getQueryParameters();
}
