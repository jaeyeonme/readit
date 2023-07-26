package me.jaeyeon.readitapi.config.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.annotation.AuthenticationRequired;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.common.exception.UnAuthenticatedAccessException;
import me.jaeyeon.readitdomain.member.service.AuthenticationUseCase;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

	private final AuthenticationUseCase authenticationUseCase;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
			Exception {
		if (handler instanceof HandlerMethod && ((HandlerMethod)handler).hasMethodAnnotation(
				AuthenticationRequired.class)) {
			Long memberId = authenticationUseCase.getLoginMemberId();

			if (memberId == null) {
				throw new UnAuthenticatedAccessException(ErrorCode.UNAUTHENTICATED_ACCESS);
			}
		}
		return true;
	}
}
