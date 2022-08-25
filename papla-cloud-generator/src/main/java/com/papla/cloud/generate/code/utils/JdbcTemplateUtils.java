package com.papla.cloud.generate.code.utils;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.ConnectionHolder;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author admin
 * @version 1.0.0
 * @ClassName DBUtils.java
 * @Description TODO
 * @createTime 2022年02月12日 23:36:00
 */
public class JdbcTemplateUtils {

    public static JdbcTemplate getJdbcTemplate(String dbType,String url,String username,String password){

        DataSourceBuilder dataSourceBuilder = DataSourceBuilder.create();
        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);
        switch (dbType){
            case "MYSQL":
                dataSourceBuilder.driverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
            break;
            case "ORACLE":
                dataSourceBuilder.driverClassName("oracle.jdbc.driver.OracleDriver");
            break;
            case "MSSQL":
                dataSourceBuilder.driverClassName("com.mircosoft.sqlserver.jdbc.SQLServerDriver");
            break;
            default:
                dataSourceBuilder.driverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
        }
        DataSource dataSource = dataSourceBuilder.build();

        return new JdbcTemplate(dataSource);
    }

}
