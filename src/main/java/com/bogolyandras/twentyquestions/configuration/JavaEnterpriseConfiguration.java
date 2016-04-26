package com.bogolyandras.twentyquestions.configuration;

import javax.naming.NamingException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jndi.JndiTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

@Configuration
public class JavaEnterpriseConfiguration {

	@Bean
	DataSource dataSource() throws NamingException {
		DataSource dataSource = null;
		JndiTemplate jndi = new JndiTemplate();
		dataSource = (DataSource) jndi.lookup("java:/twentyquestionsDS");
		return dataSource;
	}

	@Bean
	EntityManagerFactory entityManagerFactory() throws NamingException {
		EntityManagerFactory entityManagerFactory = null;
		JndiTemplate jndiTemplate = new JndiTemplate();
		entityManagerFactory = (EntityManagerFactory)
				jndiTemplate.lookup("java:comp/env/persistence/twentyquestions-emf");
		return entityManagerFactory;
	}
	
	@Bean
	TransactionManager txManager() throws NamingException {
		TransactionManager transactionManager = null;
		JndiTemplate jndi = new JndiTemplate();
		transactionManager = (TransactionManager) jndi.lookup("java:/TransactionManager");
		return transactionManager;
	}
	
	@Bean
	PlatformTransactionManager transactionManager(TransactionManager transactionManager) {
		return new JtaTransactionManager(transactionManager);
	}
	
}
