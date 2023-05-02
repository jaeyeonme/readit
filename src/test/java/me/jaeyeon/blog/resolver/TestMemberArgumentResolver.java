package me.jaeyeon.blog.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import me.jaeyeon.blog.annotation.CurrentMember;
import me.jaeyeon.blog.model.Member;
import me.jaeyeon.blog.testHelper.TestHelper;

public class TestMemberArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.getParameterAnnotation(CurrentMember.class) != null
			&& parameter.getParameterType().equals(Member.class);
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
		return TestHelper.createTestMember(1L, "Test User", "test@example.com", "password");
	}
}
