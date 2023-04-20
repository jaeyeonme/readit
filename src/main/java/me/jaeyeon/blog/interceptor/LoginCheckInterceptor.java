package me.jaeyeon.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import me.jaeyeon.blog.config.SessionConst;
import me.jaeyeon.blog.exception.ErrorCode;

public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {

		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession();

		if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
			if (requestURI.startsWith("/api/posts") && request.getMethod().equals("GET")) {
				return true;
			}

			ErrorCode errorCode = ErrorCode.UNAUTHORIZED_MEMBER;
			response.setStatus(errorCode.getHttpStatus().value());
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			response.getWriter().write(
				"{\"errorCode\":\"" + errorCode.getErrorCode() +
					"\", \"errorMessage\":\"" + errorCode.getMessage() + "\"}"
			);
			return false;
		}

		return true;
	}
}
