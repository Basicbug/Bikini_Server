package com.basicbug.bikini.likes.repository;

import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.likes.model.Likes;
import com.basicbug.bikini.likes.type.TargetType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface LikesRepository extends JpaRepository<Likes, Long> {

    @Query(value = "SELECT TARGET_ID, COUNT(*) as count FROM likes WHERE target_type = :targetType GROUP BY TARGET_ID ORDER BY count LIMIT :limit", nativeQuery = true)
    List<String> getMostLikesForTarget(TargetType targetType, int limit);

    Likes findByTargetTypeAndTargetIdAndUser(TargetType targetType, String targetId, User user);

    List<Likes> findAllByTargetTypeAndTargetId(TargetType targetType, String targetId);
}
