package com.bogolyandras.twentyquestions.configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;

import org.springframework.web.WebApplicationInitializer;

public class ServletOnlyInitializer implements WebApplicationInitializer {

	@Override
	public void onStartup(ServletContext container) throws ServletException {
	      ApplicationConfiguration.doOnStartup(container, ServletOnlyConfiguration.class);
	}

}
