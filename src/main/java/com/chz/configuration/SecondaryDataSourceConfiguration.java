package com.chz.configuration;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

/**
 * springboot + mybatis 操作多个数据源
 * **/
@Configuration
@MapperScan(basePackages = SecondaryDataSourceConfiguration.PACKAGE, sqlSessionFactoryRef = "secondarySqlSessionFactory")
public class SecondaryDataSourceConfiguration {

    protected static final String MAPPER_LOCATION = "classpath:mapper/secondary/*.xml";

    protected static final String PACKAGE = "com.chz.mapper.secondary";

    @Bean(name = "secondaryTransactionManager")
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("secondaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "secondarySqlSessionFactory")
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("secondaryDataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(SecondaryDataSourceConfiguration.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
