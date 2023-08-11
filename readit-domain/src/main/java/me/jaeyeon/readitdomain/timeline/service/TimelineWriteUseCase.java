package me.jaeyeon.readitdomain.timeline.service;

import java.util.List;

public interface TimelineWriteUseCase {

	void deliveryToTimeLine(Long postId, List<Long> followerMemberIds);
}
