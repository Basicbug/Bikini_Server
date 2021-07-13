package com.basicbug.bikini.repository;

import com.basicbug.bikini.model.Likes;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query(value = "SELECT FEED_ID, COUNT(*) as count FROM likes GROUP BY FEED_ID ORDER BY count LIMIT :limit", nativeQuery = true)
    List<Long> getMostLikesFeedIds(int limit);
}
