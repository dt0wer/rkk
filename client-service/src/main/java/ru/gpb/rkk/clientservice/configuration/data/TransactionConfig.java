package ru.gpb.rkk.clientservice.configuration.data;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.apache.commons.dbcp2.BasicDataSource;
import liquibase.integration.spring.SpringLiquibase;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@PropertySource("classpath:/application.yml")
public class TransactionConfig {
    final String DATASOURCE_BEAN="dbcpDataSource";
    final String SESSION_FACTORY_BEAN="sessionFactory";
    final String TRANSACTION_MANAGER_BEAN="transactionManager";
    final String LIQUIBASE_BEAN="liquibase";
    final String LIQUIBASE_DATASOURCE_BEAN="liquibaseDataSource";

    @Autowired
    Environment environment;

    protected Properties buildHibernateProperties()
    {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("spring.jpa.database-platform", environment.getProperty("spring.jpa.database-platform"));
        hibernateProperties.setProperty("spring.jpa.generate-ddl", environment.getProperty("spring.jpa.generate-ddl"));
        hibernateProperties.setProperty("spring.jpa.hibernate.ddl-auto", environment.getProperty("spring.jpa.hibernate.ddl-auto"));
        hibernateProperties.setProperty("spring.jpa.hibernate.use-new-id-generator-mappings", environment.getProperty("spring.jpa.hibernate.use-new-id-generator-mappings"));
        hibernateProperties.setProperty("spring.jpa.hibernate.naming.physical-strategy", environment.getProperty("spring.jpa.hibernate.naming.physical-strategy"));
        hibernateProperties.setProperty("spring.jpa.show-sql", environment.getProperty("spring.jpa.show-sql"));
        hibernateProperties.setProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults", environment.getProperty("spring.jpa.properties.hibernate.temp.use_jdbc_metadata_defaults"));
        return hibernateProperties;
    }

    @Bean(name = DATASOURCE_BEAN, destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.dbcp2", ignoreUnknownFields = false)
    public DataSource dataSource() {
        BasicDataSource dbcp = new BasicDataSource();
        return dbcp;
    }

    @Bean(name = SESSION_FACTORY_BEAN)
    @DependsOn(LIQUIBASE_BEAN)
    @Autowired(required=true)
    public LocalSessionFactoryBean sessionFactoryBean (@Qualifier(DATASOURCE_BEAN) DataSource dataSource, Environment environment) {
       LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
       factoryBean.setHibernateProperties(buildHibernateProperties());
       factoryBean.setPackagesToScan("ru.gpb.rkk.clientservice.domain");
       factoryBean.setDataSource(dataSource);
       return factoryBean;

    }

    @Bean (name = TRANSACTION_MANAGER_BEAN)
    @Autowired(required=true)
    public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }

    @Bean (name = LIQUIBASE_DATASOURCE_BEAN)
    DataSource liquibaseDatasource (){
        return DataSourceBuilder.create().
                username(environment.getProperty("spring.liquibase.user")).
                password(environment.getProperty("spring.liquibase.password")).
                url(environment.getProperty("spring.liquibase.url")).
                build();
    }
    @Bean (name = LIQUIBASE_BEAN)
    public SpringLiquibase liquibase(@Qualifier(LIQUIBASE_DATASOURCE_BEAN) DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog(environment.getProperty("spring.liquibase.change-log"));
        Map<String, String> map = new HashMap<>();
        map.put("spring.liquibase.check-change-log-location", environment.getProperty("spring.liquibase.check-change-log-location"));
        liquibase.setChangeLogParameters(map);
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
