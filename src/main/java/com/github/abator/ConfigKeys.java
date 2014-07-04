package com.github.abator;

public final class ConfigKeys {

    public static final String OUTPUT            = "output";
    public static final String CONFIG_PREFIX     = "abator.";
    public static final String DB_URL            = CONFIG_PREFIX + "db.url";
    public static final String DB_NAME           = CONFIG_PREFIX + "db.name";
    public static final String DB_USER           = CONFIG_PREFIX + "db.user";
    public static final String DB_PASSWORD       = CONFIG_PREFIX + "db.password";

    public static final String DAO_IMPL_IMPORT   = CONFIG_PREFIX + "dao.impl.import";

    public static final String DAO_PACKAGE       = CONFIG_PREFIX + "dao.package";
    public static final String DAO_SUFFIX        = CONFIG_PREFIX + "dao.suffix";
    public static final String DAO_EXTENDS       = CONFIG_PREFIX + "dao.extends";

    public static final String DOMAIN_PACKAGE    = CONFIG_PREFIX + "domain.package";
    public static final String DOMAIN_SUFFIX     = CONFIG_PREFIX + "domain.suffix";
    public static final String DOMAIN_EXTENDS    = CONFIG_PREFIX + "domain.extends";

    public static final String DAO_IMPL_PACKAGE  = CONFIG_PREFIX + "dao.impl.package";
    public static final String DAO_IMPL_EXTENDS  = CONFIG_PREFIX + "dao.impl.extends";
    public static final String DAO_IMPL_SUFFIX   = CONFIG_PREFIX + "dao.impl.suffix";
    public static final String SQL_MAPPER_SUFFIX = CONFIG_PREFIX + "sql.mapper.suffix";
    public static final String HANDLE_TABLES     = CONFIG_PREFIX + "tables";
}
