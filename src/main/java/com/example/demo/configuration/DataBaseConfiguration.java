package com.example.demo.configuration;

import java.util.Properties;

import javax.sql.DataSource;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DataBaseConfiguration {

	@Bean	
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean	 sessionfactoryBean = new LocalSessionFactoryBean();
		sessionfactoryBean.setDataSource(dataSource());
		sessionfactoryBean.setPackagesToScan("com.example.demo.model");
		sessionfactoryBean.setHibernateProperties(hibernateProperties());
		return sessionfactoryBean;
	}
	
	@Bean
	public DataSource dataSource(){
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUrl("jdbc:mysql://localhost:3306/platziprofesores");
		dataSource.setUsername("platziprofesores");
		dataSource.setPassword("platziprofesores");
		return dataSource;
	}
	
	public Properties hibernateProperties(){
		Properties properties = new Properties();
		properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		properties.put("hibernate.show_sql", "true");

		return properties;
	}
	
	@Bean
	@Autowired
	public HibernateTransactionManager transactionManager(){
		HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
		hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
		return hibernateTransactionManager;
	}

}
