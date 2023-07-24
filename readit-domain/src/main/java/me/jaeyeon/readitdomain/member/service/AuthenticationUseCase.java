package me.jaeyeon.readitdomain.member.service;

import me.jaeyeon.readitdomain.member.domain.Member;

public interface AuthenticationUseCase {

	void login(long id);

	void logout();

	Member getLoginMember();

	Long getLoginMemberId();
}
