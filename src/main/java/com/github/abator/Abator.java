package com.github.abator;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.abator.builder.*;
import com.github.abator.utils.FileUtils;

/**
 * @author xiebiao
 */
public final class Abator {

  private static final Logger logger = LoggerFactory.getLogger(Abator.class);
  private static Config CONFIG;

  static {
    try {
      CONFIG = Config.getInstance();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void init() {
    FileUtils.delete(CONFIG.getOutput());
  }

  public void generate() {
    this.init();
    MapperBuilder sqlMapperBuilder = new MapperBuilder(CONFIG);
    for (Table table : CONFIG.getTables()) {
      DomainClassBuilder domainClassBuilder = new DomainClassBuilder(table);
      domainClassBuilder.setConfig(CONFIG);
      DaoClassBuilder daoClassBuilder = new DaoClassBuilder(table.getName());
      daoClassBuilder.setConfig(CONFIG);

      try {
        String domainName = domainClassBuilder.from(table).build();
        String daoName = daoClassBuilder.build();
        DaoImplClassBuilder daoImplClassBuilder = new DaoImplClassBuilder(daoName);
        daoImplClassBuilder.setConfig(CONFIG);
        daoImplClassBuilder.setInterfaces(daoName);

        sqlMapperBuilder.setDomain(domainName);
        String namespace = daoImplClassBuilder.build();
        sqlMapperBuilder.setNamespace(namespace);
        sqlMapperBuilder.from(table).build();
      } catch (Exception e) {
        logger.error("{}", e);
      }
    }
  }
}
