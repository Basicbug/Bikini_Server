package com.basicbug.bikini.repository;

import com.basicbug.bikini.model.Feed;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Feed findByFeedId(UUID feedId);

    @Modifying
    @Transactional
    long deleteByFeedId(UUID feedId);
}
