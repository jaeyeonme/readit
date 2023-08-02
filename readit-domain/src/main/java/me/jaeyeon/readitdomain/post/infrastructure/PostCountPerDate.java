package me.jaeyeon.readitdomain.post.infrastructure;

import java.time.LocalDate;

public record PostCountPerDate(
		LocalDate date,
		long count
) {
}
