package com.github.abator;

import java.io.IOException;

import com.github.abator.builder.*;
import com.github.abator.utils.FileUtils;

/**
 * 工具入口
 * @author xiebiao[谢彪]
 */
public class Abator {

    public static Config CONFIG;

    static {
        try {
            CONFIG = Config.getInstance();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void init() {
        FileUtils.delete(CONFIG.getOutput());
    }

    public static void main(String[] args) {
        Abator.init();
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
                e.printStackTrace();

            }
        }
    }
}
