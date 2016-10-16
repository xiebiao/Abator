package com.github.abator.database;

import java.util.Comparator;

/**
 * @author <a href="mailto:joyrap@qq.com">joyrap@qq.com</a>
 * @date 6/22/15
 */
public class ColumnComparator implements Comparator<Column> {
  @Override
  public int compare(Column o1, Column o2) {
    if (o1.isPrimaryKey()) {
      return -1;
    }
    return 0;
  }
}
