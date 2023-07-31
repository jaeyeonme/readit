package me.jaeyeon.readitdomain.member.service;

import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.domain.MemberCreate;

public interface MemberWriteUseCase {
	Member register(MemberCreate request);
	Member updateUserName(Long memberId, String userName);
}
