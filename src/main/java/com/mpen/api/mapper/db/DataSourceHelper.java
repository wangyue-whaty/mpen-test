/**
 * Copyright (C) 2016 MPen, Inc. All Rights Reserved.
 */

package com.mpen.api.mapper.db;

import javax.sql.DataSource;

import org.apache.tomcat.jdbc.pool.PoolProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataSourceHelper {

    static Logger log = LoggerFactory.getLogger(DataSourceHelper.class);

    public static DataSource getDataSource(DataSourceConfig config) {
        return mysqlDataSource(config);

    }

    private static DataSource mysqlDataSource(DataSourceConfig config) {

        PoolProperties prop = new PoolProperties();
        prop.setUrl(config.databaseUrl);
        prop.setDriverClassName("com.mysql.jdbc.Driver");
        prop.setUsername(config.databaseUsername);
        prop.setPassword(config.databasePassword);
        // when database broke, reconnect
        prop.setTestOnBorrow(true);
        prop.setValidationQuery("SELECT 1");
        prop.setValidationInterval(1000);

        prop.setMaxActive(500);
        // idle connections survive 60s by default
        prop.setMaxIdle(10);
        // if idle for long time, we don't keep them
        prop.setMinIdle(0);
        prop.setMaxWait(10000);
        // If connection is broken
        prop.setLogAbandoned(true);
        prop.setRemoveAbandoned(true);
        prop.setRemoveAbandonedTimeout(60);
        prop.setJdbcInterceptors("org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
        org.apache.tomcat.jdbc.pool.DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setPoolProperties(prop);
        return dataSource;
    }
}
