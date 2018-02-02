package com.na.user.socketserver.config;

import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import com.na.user.socketserver.aop.I18NInterceptor;

/**
 * Mybatis配置
 * 
 * @author alan
 * @date 2017年5月30日 下午4:33:35
 */
@Configuration
public class MybatisConfig {
	
	@Autowired
	private Environment environment;
	
	/**
     * 根据数据源创建SqlSessionFactory
     */
    @Bean(name="sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(DynamicDataSource ds) throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        //设置mybatis configuration 扫描路径
        sessionFactory  
        .setConfigLocation(new DefaultResourceLoader()  
                .getResource(environment  
                        .getProperty("mybatis.configLocation")));
        //指定数据源(这个必须有，否则报错)
        sessionFactory.setDataSource(ds);
        //下边两句仅仅用于*.xml文件，如果整个持久层操作不需要使用到xml文件的话（只用注解就可以搞定），则不加
        //指定基包
        sessionFactory.setTypeAliasesPackage(environment.getProperty("mybatis.typeAliasesPackage"));
        //添加mapper 扫描路径
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
        			.getResources(environment.getProperty("mybatis.mapperLocations")));
//        sessionFactory.setPlugins(new Interceptor[]{new I18NInterceptor()});
        
        return sessionFactory.getObject();
    }
    
    /**
     * 配置事务管理器
     */
    @Bean
    public DataSourceTransactionManager transactionManager(DynamicDataSource dataSource) throws Exception {
        return new DataSourceTransactionManager(dataSource);
    }

}
