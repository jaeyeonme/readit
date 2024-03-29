package me.jaeyeon.readitapi.config.resolver;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.annotation.CurrentMember;
import me.jaeyeon.readitdomain.member.service.AuthenticationUseCase;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	private final AuthenticationUseCase authenticationUseCase;

	@Override
	public boolean supportsParameter(MethodParameter methodParameter) {
		return methodParameter.hasParameterAnnotation(CurrentMember.class);
	}

	@Override
	public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
			NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
		return authenticationUseCase.getLoginMember();
	}
}
