package com.na.user.socketserver.config;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;

/**
 * 数据库连接池配置。
 * Created by sunny on 2017/5/18 0018.
 */
@Configuration
public class DatabaseConfiguration implements EnvironmentAware {
    private Environment environment;
    private RelaxedPropertyResolver propertyResolver;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
        this.propertyResolver = new RelaxedPropertyResolver(environment,"spring.datasource.");
    }
    
    //注册dataSource
    @Primary
    @Bean(name="writeDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource writeDataSource() throws SQLException {
        if (StringUtils.isEmpty(propertyResolver.getProperty("url"))) {
            System.out.println("Your database connection pool configuration is incorrect!"
                    + " Please check your Spring profile, current profiles are:"
                    + Arrays.toString(environment.getActiveProfiles()));
            throw new ApplicationContextException(
                    "Database connection pool is not configured correctly");
        }
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName(propertyResolver.getProperty("driver-class-name"));
        dataSource.setUrl(propertyResolver.getProperty("url"));
        dataSource.setUsername(propertyResolver.getProperty("username"));
        dataSource.setPassword(propertyResolver.getProperty("password"));
        dataSource.setInitialSize(Integer.parseInt(propertyResolver.getProperty("initialSize")));
        dataSource.setMinIdle(Integer.parseInt(propertyResolver.getProperty("minIdle")));
        dataSource.setMaxActive(Integer.parseInt(propertyResolver.getProperty("maxActive")));
        dataSource.setMaxWait(Integer.parseInt(propertyResolver.getProperty("maxWait")));
        dataSource.setTimeBetweenEvictionRunsMillis(Long.parseLong(propertyResolver.getProperty("timeBetweenEvictionRunsMillis")));
        dataSource.setMinEvictableIdleTimeMillis(Long.parseLong(propertyResolver.getProperty("minEvictableIdleTimeMillis")));
        dataSource.setValidationQuery(propertyResolver.getProperty("validationQuery"));
        dataSource.setTestWhileIdle(Boolean.parseBoolean(propertyResolver.getProperty("testWhileIdle")));
        dataSource.setTestOnBorrow(Boolean.parseBoolean(propertyResolver.getProperty("testOnBorrow")));
        dataSource.setTestOnReturn(Boolean.parseBoolean(propertyResolver.getProperty("testOnReturn")));
        dataSource.setPoolPreparedStatements(Boolean.parseBoolean(propertyResolver.getProperty("poolPreparedStatements")));
        dataSource.setMaxPoolPreparedStatementPerConnectionSize(Integer.parseInt(propertyResolver.getProperty("maxPoolPreparedStatementPerConnectionSize")));
//        dataSource.setFilters(propertyResolver.getProperty("filters"));
        
        List<Filter> filters = new ArrayList<>();
        filters.add(wallFilter());
        dataSource.setProxyFilters(filters);
        
        return dataSource;
    }
    
    @Bean
    public WallFilter wallFilter() {
        WallFilter wallFilter=new WallFilter();
        wallFilter.setConfig(wallConfig());
        return  wallFilter;
    }
    @Bean
    public WallConfig wallConfig(){
        WallConfig config =new WallConfig();
        //允许一次执行多条语句
        config.setMultiStatementAllow(propertyResolver.getProperty("wall.wallconfig.multiStatementAllow",Boolean.class));
        //允许非基本语句的其他语句
//        config.setNoneBaseStatementAllow(true);
        return config;
    }
    
    @Bean(name="readDataSource",initMethod = "init", destroyMethod = "close")
    public DataSource readDataSource() {
    	if (StringUtils.isEmpty(propertyResolver.getProperty("url"))) {
            System.out.println("Your database connection pool configuration is incorrect!"
                    + " Please check your Spring profile, current profiles are:"
                    + Arrays.toString(environment.getActiveProfiles()));
            throw new ApplicationContextException(
                    "Database connection pool is not configured correctly");
        }
    	DruidDataSource dataSource = new DruidDataSource();
    	dataSource.setDriverClassName(propertyResolver.getProperty("read.driver-class-name"));
    	dataSource.setUrl(propertyResolver.getProperty("read.url"));
    	dataSource.setUsername(propertyResolver.getProperty("read.username"));
    	dataSource.setPassword(propertyResolver.getProperty("read.password"));
    	return dataSource;
    }
    
    /**
     * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
     * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
     */
    @Bean
//    @Primary
    public DynamicDataSource dataSource(
    		@Qualifier("writeDataSource") DataSource writeDataSource,
            @Qualifier("readDataSource") DataSource readDataSourceList) {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put(DatabaseContextHolder.MASTER, writeDataSource);
        targetDataSources.put(DatabaseContextHolder.SLAVE, readDataSourceList);
        
        DynamicDataSource dataSource = new DynamicDataSource();
        // 该方法是AbstractRoutingDataSource的方法
        dataSource.setTargetDataSources(targetDataSources);
        // 默认的datasource设置为myTestDbDataSource
        dataSource.setDefaultTargetDataSource(writeDataSource);
        return dataSource;
    }
    
}
