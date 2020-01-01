package com.chz.template;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
@Configuration
public class MultiJDBCTemplate {

    @Bean(name="primaryJDBCTemplate")
    public JdbcTemplate primaryJDBCTemplate(@Qualifier(value="primaryDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }

    @Bean(name="secondaryJDBCTemplate")
    public JdbcTemplate secondaryJDBCTemplate(@Qualifier(value="secondaryDataSource") DataSource dataSource){
        return new JdbcTemplate(dataSource);
    }
}
