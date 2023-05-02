package me.jaeyeon.blog.testHelper;

import me.jaeyeon.blog.model.Member;

public class TestHelper {
	public static Member createTestMember(Long id, String userName, String email, String password) {
		Member member = new Member(userName, email, password);
		member.setId(id);
		return member;
	}
}

