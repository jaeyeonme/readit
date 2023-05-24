package me.jaeyeon.blog.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.blog.annotation.AuthenticationRequired;
import me.jaeyeon.blog.exception.ErrorCode;
import me.jaeyeon.blog.exception.UnAuthenticatedAccessException;
import me.jaeyeon.blog.service.LoginService;

@Component
@RequiredArgsConstructor
public class LoginCheckInterceptor implements HandlerInterceptor {

	private final LoginService loginService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
			Exception {
		if (handler instanceof HandlerMethod && ((HandlerMethod)handler).hasMethodAnnotation(
				AuthenticationRequired.class)) {
			Long memberId = loginService.getLoginMemberId();

			if (memberId == null) {
				throw new UnAuthenticatedAccessException(ErrorCode.UNAUTHENTICATED_ACCESS);
			}
		}
		return true;
	}
}
