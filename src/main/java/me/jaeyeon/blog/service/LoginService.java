package me.jaeyeon.blog.service;

import me.jaeyeon.blog.model.Member;

public interface LoginService {
	void login(long id);

	void logout();

	Member getLoginMember();

	Long getLoginMemberId();
}
