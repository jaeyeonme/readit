package me.jaeyeon.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.OAS_30).select()
				.apis(RequestHandlerSelectors.basePackage("me.jaeyeon.blog")) // API 패키지 경로
				.paths(PathSelectors.ant("/api/**")) // path 조건에 따라서 API 문서화
				.build()
				.apiInfo(apiInfo()) // API 문서에 대한 정보 추가
				.useDefaultResponseMessages(false) // swagger에서 제공하는 기본 응답 코드 설명
				;
	}

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title("Spring Boot Blog").description("블로그 API 명세서").version("1.0").build();
	}
}
