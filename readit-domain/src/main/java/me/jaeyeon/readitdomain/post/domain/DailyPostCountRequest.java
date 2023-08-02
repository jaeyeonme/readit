package me.jaeyeon.readitdomain.post.domain;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

public record DailyPostCountRequest(
		Long memberId,

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate firstDate,

		@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
		LocalDate lastDate) {
}
