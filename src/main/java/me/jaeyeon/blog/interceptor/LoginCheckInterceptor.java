package me.jaeyeon.blog.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;

import me.jaeyeon.blog.config.SessionConst;

public class LoginCheckInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
		Object handler) throws Exception {

		String requestURI = request.getRequestURI();
		HttpSession session = request.getSession();

		if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
			response.sendRedirect("/members/signin?redirectURL=" + requestURI);
			return false;
		}

		return true;
	}
}
