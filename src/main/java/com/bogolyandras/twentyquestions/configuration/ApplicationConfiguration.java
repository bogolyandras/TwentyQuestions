package com.bogolyandras.twentyquestions.configuration;

import com.bogolyandras.twentyquestions.persistence.dao.DAOMarker;
import com.bogolyandras.twentyquestions.service.ServiceMarker;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import javax.sql.DataSource;
import java.util.EnumSet;

@Configuration
@ComponentScan(basePackageClasses = {ServiceMarker.class})
@EnableTransactionManagement
@EnableJpaRepositories(basePackageClasses = DAOMarker.class)
@EnableWebMvc
@EnableSpringDataWebSupport
public class ApplicationConfiguration {
	
	@Bean
	public SpringLiquibase liquibase(DataSource dataSource) {
		SpringLiquibase springLiquibase = new SpringLiquibase();
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog("classpath:META-INF/migration/DatabaseChangeLog.xml");
		return springLiquibase;
	}
	
	@Bean
	public LocalValidatorFactoryBean validator() {
		return new LocalValidatorFactoryBean();
	}
	
	static void doOnStartup(ServletContext container, Class<?> ConfigurationToLoad) {
		//<editor-fold desc="Application Rootcontext">
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
	    rootContext.register(ApplicationConfiguration.class, SecurityConfiguration.class);
	    rootContext.register(ConfigurationToLoad);
	    container.addListener(new ContextLoaderListener(rootContext));
		//</editor-fold>

		//<editor-fold desc="UTF-8 Encoding filter">
		FilterRegistration.Dynamic characterEncodingFilter = container.addFilter("characterEncodingFilter", CharacterEncodingFilter.class);
		characterEncodingFilter.setInitParameter("encoding", "UTF-8");
		characterEncodingFilter.setInitParameter("forceEncoding", "true");
		characterEncodingFilter.addMappingForUrlPatterns(null, false, "/*");
		//</editor-fold>

		//<editor-fold desc="Spring Security Filter">
		FilterRegistration.Dynamic springSecurityFilterChain =
				container.addFilter("springSecurityFilterChain", DelegatingFilterProxy.class);
		EnumSet<DispatcherType> dispatcherTypes = EnumSet.of(DispatcherType.REQUEST, DispatcherType.ERROR);
		springSecurityFilterChain.addMappingForUrlPatterns(dispatcherTypes, false, "/*");
		//</editor-fold>

		//<editor-fold desc="DefaultServlet">
		AnnotationConfigWebApplicationContext defaultServletContext = new AnnotationConfigWebApplicationContext();
		defaultServletContext.register(DefaultServletConfiguration.class);

		ServletRegistration.Dynamic defaultServlet =
				container.addServlet("DefaultServlet", new DispatcherServlet(defaultServletContext));
		defaultServlet.setLoadOnStartup(0);
		defaultServlet.addMapping("/");
		//</editor-fold>
	}
	
}
