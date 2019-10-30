package com.bogdan.persistentweb.errorhandling;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.web.embedded.jetty.JettyServletWebServerFactory;
import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.DispatcherServlet;

@Configuration
public class DispatcherServletConfigurer implements BeanPostProcessor {

  public Object postProcessBeforeInitialization(final Object bean, final String beanName) throws BeansException {
    if (bean instanceof DispatcherServlet) {
      ((DispatcherServlet) bean).setThrowExceptionIfNoHandlerFound(true);
    }
    return bean;
  }

  public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
    return bean;
  }

  @Bean
  public WebServerFactoryCustomizer<JettyServletWebServerFactory> containerCustomizer() {
    return container -> {
      ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/resourceNotFound");
      container.addErrorPages(error404Page);
    };
  }
}