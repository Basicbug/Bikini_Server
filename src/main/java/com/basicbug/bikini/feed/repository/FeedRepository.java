package com.basicbug.bikini.feed.repository;

import com.basicbug.bikini.feed.model.Feed;
import java.util.List;
import java.util.UUID;

import com.basicbug.bikini.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface FeedRepository extends JpaRepository<Feed, Long> {

    Feed findByFeedId(UUID feedId);

    List<Feed> findByUser(User user);

    @Modifying
    @Transactional
    long deleteByFeedId(UUID feedId);

    String HAVERSINE_FORMULA = "(6371 * acos(cos(radians(:latitude)) * cos(radians(f.location.latitude)) *" +
        " cos(radians(f.location.longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(f.location.latitude))))";

    @Query("SELECT f FROM Feed f WHERE " + HAVERSINE_FORMULA + " < :distance ORDER BY "+ HAVERSINE_FORMULA + " DESC")
    List<Feed> findFeedsNearLocation(@Param("longitude") double longitude, @Param("latitude") double latitude, @Param("distance") double radius);
}
