package me.jaeyeon.readitdomain.member.service;

public interface SessionManager {

	void setAttribute(String name, Object value);
	Object getAttribute(String name);
	void removeAttribute(String name);
}
