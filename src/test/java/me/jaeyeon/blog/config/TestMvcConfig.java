package me.jaeyeon.blog.config;

import java.util.List;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import me.jaeyeon.blog.resolver.TestMemberArgumentResolver;

@TestConfiguration
public class TestMvcConfig implements WebMvcConfigurer {

	@Bean
	public TestMemberArgumentResolver testMemberArgumentResolver() {
		return new TestMemberArgumentResolver();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(testMemberArgumentResolver());
	}
}
