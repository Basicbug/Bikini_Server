package com.basicbug.bikini.repository;

import com.basicbug.bikini.model.User;
import com.basicbug.bikini.model.likes.Likes;
import com.basicbug.bikini.model.likes.TargetType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query(value = "SELECT TARGET_ID, COUNT(*) as count FROM likes WHERE target_type = :targetType GROUP BY TARGET_ID ORDER BY count LIMIT :limit", nativeQuery = true)
    List<String> getMostLikesForTarget(TargetType targetType, int limit);

    Likes findByTargetTypeAndTargetIdAndUser(TargetType targetType, String targetId, User user);
}
