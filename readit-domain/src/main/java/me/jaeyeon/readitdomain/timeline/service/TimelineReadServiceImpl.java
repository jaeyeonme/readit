package me.jaeyeon.readitdomain.timeline.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.timeline.domain.Timeline;
import me.jaeyeon.readitdomain.timeline.service.port.TimelineRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TimelineReadServiceImpl implements TimelineReadUseCase {

	private final TimelineRepository timelineRepository;

	@Override
	public List<Timeline> getTimelinesForMember(Member member, int size) {
		return timelineRepository.findByMember(member, size);
	}

	@Override
	public List<Timeline> getNextTimelinesForMember(Long lastId, Member member, int size) {
		return timelineRepository.findByIdLessThanAndMember(lastId, member, size);
	}
}
