package me.jaeyeon.readitapi.post.controller.response;

import java.time.LocalDate;

public record DailyPostCount(
		Long memberId,
		LocalDate date,
		Long postCount
) {

	public static DailyPostCount from(Long memberId, LocalDate date, Long postCount) {
		return new DailyPostCount(memberId, date, postCount);
	}
}
