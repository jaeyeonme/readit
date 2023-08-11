package me.jaeyeon.readitdomain.timeline.infrastructure;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.timeline.domain.Timeline;
import me.jaeyeon.readitdomain.timeline.service.port.TimelineRepository;

@Repository
@RequiredArgsConstructor
public class TimelineRepositoryImpl implements TimelineRepository {

	private final TimelineJpaRepository timelineJpaRepository;

	@Override
	public List<Timeline> findByMember(Member member, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdDate"));
		return timelineJpaRepository.findByMemberOrderByCreatedDateDesc(member.toEntity(), pageable)
				.stream()
				.map(Timeline::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public List<Timeline> findByIdLessThanAndMember(Long id, Member member, int size) {
		Pageable pageable = PageRequest.of(0, size, Sort.by(Sort.Direction.DESC, "createdDate"));
		return timelineJpaRepository.findByIdLessThanAndMemberOrderByCreatedDateDesc(id, member.toEntity(), pageable)
				.stream()
				.map(Timeline::fromEntity)
				.collect(Collectors.toList());
	}

	@Override
	public Timeline save(Timeline timeline) {
		TimelineEntity savedEntity = timelineJpaRepository.save(timeline.toEntity());
		return Timeline.fromEntity(savedEntity);
	}

	@Override
	public void bulkInsert(List<Timeline> timelines) {
		List<TimelineEntity> entities = timelines.stream()
				.map(Timeline::toEntity)
				.collect(Collectors.toList());
		timelineJpaRepository.saveAll(entities);
	}
}
