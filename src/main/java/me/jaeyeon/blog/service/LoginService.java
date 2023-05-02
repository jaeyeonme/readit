package me.jaeyeon.blog.service;

import me.jaeyeon.blog.model.Member;

public interface LoginService {
	void login(String email, String password);
	void logout();
	Member getLoginMember();
	Long getLoginMemberId();
}
