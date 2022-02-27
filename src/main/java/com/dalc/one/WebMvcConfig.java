package com.dalc.one;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	private static final String[] CLASSPATH_RESOURCE_LOCATIONS = { "classpath:/static/", "classpath:/public/",
			"classpath:/", "classpath:/resources/", "classpath:/META-INF/resources/",
			"classpath:/META-INF/resources/webjars/", "classpath:/templates/", "classpath:/static/vue/"};
	
	//front 구현을 위한 임시 설정
	@Override
	  public void addCorsMappings(CorsRegistry registry) {
	    registry.addMapping("/**")
	        .allowedOrigins("http://localhost:8081")
	        .exposedHeaders("authorization")	//make client read header("jwt-token")
	        .allowedMethods(
	            	HttpMethod.GET.name(),
	            	HttpMethod.HEAD.name(),
	            	HttpMethod.POST.name(),
	            	HttpMethod.PUT.name(),
	            	HttpMethod.DELETE.name());
            ;
	}
	
	@Override
	public void addResourceHandlers(final ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
	}
}