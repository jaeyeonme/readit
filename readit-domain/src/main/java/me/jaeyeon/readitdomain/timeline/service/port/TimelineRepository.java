package me.jaeyeon.readitdomain.timeline.service.port;

import java.util.List;

import me.jaeyeon.readitdomain.member.domain.Member;
import me.jaeyeon.readitdomain.timeline.domain.Timeline;

public interface TimelineRepository {

	List<Timeline> findByMember(Member member, int size);
	List<Timeline> findByIdLessThanAndMember(Long id, Member member, int size);
	Timeline save(Timeline timeline);
	void bulkInsert(List<Timeline> timelines);
}
