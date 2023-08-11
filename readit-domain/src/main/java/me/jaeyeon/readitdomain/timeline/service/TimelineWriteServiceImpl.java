package me.jaeyeon.readitdomain.timeline.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.common.exception.BlogApiException;
import me.jaeyeon.common.exception.ErrorCode;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.member.service.port.MemberRepository;
import me.jaeyeon.readitdomain.post.domain.Post;
import me.jaeyeon.readitdomain.post.service.port.PostRepository;
import me.jaeyeon.readitdomain.timeline.domain.Timeline;
import me.jaeyeon.readitdomain.timeline.service.port.TimelineRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class TimelineWriteServiceImpl implements TimelineWriteUseCase {

	private final TimelineRepository timelineRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;

	@Override
	public void deliveryToTimeLine(Long postId, List<Long> followerMemberIds) {
		// postId를 통해 Post 객체를 조회
		Post post = postRepository.findById(postId)
				.orElseThrow(() -> new BlogApiException(ErrorCode.POST_NOT_FOUND));

		// 각 팔로워를 위한 타임라인 객체를 생성
		List<Timeline> timelines = followerMemberIds.stream()
				.map(memberId -> {
					Member member = memberRepository.findById(memberId)
							.orElseThrow(() -> new BlogApiException(ErrorCode.MEMBER_NOT_FOUND));
					return Timeline.builder()
							.post(post)
							.member(member)
							.build();
				})
				.collect(Collectors.toList());

		timelineRepository.bulkInsert(timelines);
	}
}

