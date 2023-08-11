package me.jaeyeon.readitdomain.timeline.service;

import java.util.List;

import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.timeline.domain.Timeline;

public interface TimelineReadUseCase {
	List<Timeline> getTimelinesForMember(Member member, int size);
	List<Timeline> getNextTimelinesForMember(Long lastId, Member member, int size);
}
