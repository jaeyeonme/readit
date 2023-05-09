package me.jaeyeon.blog.config;

import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.interceptor.LoginCheckInterceptor;
import me.jaeyeon.blog.resolver.LoginMemberArgumentResolver;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

	private final LoginCheckInterceptor loginCheckInterceptor;
	private final LoginMemberArgumentResolver loginMemberArgumentResolver;

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(loginCheckInterceptor)
				.addPathPatterns("/**")
				.excludePathPatterns("/members/register", "/members/sign-in", "/api/posts", "/api/posts/*");
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(loginMemberArgumentResolver);
	}
}
