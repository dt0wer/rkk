package ru.gpb.rkk.clientservice.configuration.data;


import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.apache.commons.dbcp2.BasicDataSource;
import liquibase.integration.spring.SpringLiquibase;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
//@PropertySource("classpath:/hibernate/hibernate.properties")
public class TransactionConfig {
    final String DATASOURCE_BEAN="dbcpDataSource";
    final String SESSION_FACTORY_BEAN="sessionFactory";
    final String TRANSACTION_MANAGER_BEAN="transactionManager";

    protected Properties buildHibernateProperties(Environment environment)
    {
        Properties hibernateProperties = new Properties();
        /*
        hibernateProperties.setProperty("hibernate.dialect", environment.getProperty("hibernate.dialect"));
        hibernateProperties.setProperty("hibernate.jdbc.fetch_size", environment.getProperty("hibernate.jdbc.fetch_size"));
        hibernateProperties.setProperty("hibernate.jdbc.batch_size", environment.getProperty("hibernate.jdbc.batch_size"));
        hibernateProperties.setProperty("hibernate.show_sql", environment.getProperty("hibernate.show_sql"));
        hibernateProperties.setProperty("hibernate.format_sql", environment.getProperty("hibernate.format_sql"));
        */
        return hibernateProperties;
    }

    @Bean(name=DATASOURCE_BEAN, destroyMethod = "close")
    @ConfigurationProperties(prefix = "spring.datasource.dbcp2", ignoreUnknownFields = false)
    public DataSource dataSource() {
        BasicDataSource dbcp=new BasicDataSource();
        return dbcp;
    }
    @Bean(name=SESSION_FACTORY_BEAN)
    @Autowired(required=true)
    public LocalSessionFactoryBean sessionFactoryBean (@Qualifier(DATASOURCE_BEAN) DataSource dataSource, Environment environment) {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource);
        factoryBean.setHibernateProperties(buildHibernateProperties(environment));
       // factoryBean.setPackagesToScan("com.spring.core.domain");
        return factoryBean;

    }

    @Bean (name=TRANSACTION_MANAGER_BEAN)
    @Autowired(required=true)
    public PlatformTransactionManager txManager(SessionFactory sessionFactory) {
        return new HibernateTransactionManager(sessionFactory);
    }
    @Bean
    public SpringLiquibase liquibase(@Qualifier(DATASOURCE_BEAN) DataSource dataSource) {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:liquibase.scripts/changelog.xml");
        liquibase.setDataSource(dataSource);
        return liquibase;
    }
}
