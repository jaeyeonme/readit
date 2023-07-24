package me.jaeyeon.readitapi.config.web;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.member.service.SessionManager;

@Service
@RequiredArgsConstructor
public class HttpSessionManager implements SessionManager {

	private final HttpSession httpSession;

	@Override
	public void setAttribute(String name, Object value) {
		httpSession.setAttribute(name, value);
	}

	@Override
	public Object getAttribute(String name) {
		return httpSession.getAttribute(name);
	}

	@Override
	public void removeAttribute(String name) {
		httpSession.removeAttribute(name);
	}
}
