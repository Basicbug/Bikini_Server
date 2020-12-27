package com.basicbug.bikini.repository;

import com.basicbug.bikini.model.Feed;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FeedRepository extends JpaRepository<Feed, Long> {

}
