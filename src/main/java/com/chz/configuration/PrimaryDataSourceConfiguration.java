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
// 扫描 Mapper 接口并容器管理
@MapperScan(basePackages = PrimaryDataSourceConfiguration.PACKAGE, sqlSessionFactoryRef = "primarySqlSessionFactory")
public class PrimaryDataSourceConfiguration {
    protected static final String MAPPER_LOCATION = "classpath:mapper/primary/*.xml";

    protected static final String PACKAGE = "com.chz.mapper.primary";

    @Bean(name = "primaryTransactionManager")
    @Primary
    public DataSourceTransactionManager masterTransactionManager(@Qualifier("primaryDataSource") DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "primarySqlSessionFactory")
    @Primary
    public SqlSessionFactory masterSqlSessionFactory(@Qualifier("primaryDataSource") DataSource dataSource)
            throws Exception {
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources(PrimaryDataSourceConfiguration.MAPPER_LOCATION));
        return sessionFactory.getObject();
    }
}
