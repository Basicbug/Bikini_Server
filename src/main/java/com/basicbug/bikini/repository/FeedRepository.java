package com.basicbug.bikini.repository;

import com.basicbug.bikini.entity.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
