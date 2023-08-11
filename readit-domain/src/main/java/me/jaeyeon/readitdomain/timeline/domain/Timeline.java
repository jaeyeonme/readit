package me.jaeyeon.readitdomain.timeline.domain;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.timeline.infrastructure.TimelineEntity;

@Getter
public class Timeline {

	private Long id;
	private Member member;
	private Post post;
	private LocalDateTime createdDate;

	@Builder
	public Timeline(Long id, Member member, Post post, LocalDateTime createdDate) {
		this.id = id;
		this.member = member;
		this.post = post;
		this.createdDate = createdDate;
	}

	public static Timeline fromEntity(TimelineEntity timelineEntity) {
		return Timeline.builder()
				.id(timelineEntity.getId())
				.member(timelineEntity.getMember().toModel())
				.post(timelineEntity.getPost().toModel())
				.createdDate(timelineEntity.getCreatedDate())
				.build();
	}

	public TimelineEntity toEntity() {
		return TimelineEntity.builder()
				.member(this.member.toEntity())
				.post(this.post.toEntity())
				.build();
	}
}
