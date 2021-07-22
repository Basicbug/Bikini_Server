package com.basicbug.bikini.service;

import com.basicbug.bikini.model.Feed;
import com.basicbug.bikini.model.Likes;
import com.basicbug.bikini.model.User;
import com.basicbug.bikini.repository.LikesRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;

    public List<Long> getMostLikesFeedIds(int limit) {
        return likesRepository.getMostLikesFeedIds(limit);
    }

    public boolean addLikesToFeed(Feed feed, User user) {
        if (likesRepository.findByFeedAndUser(feed, user) != null) {
            log.info("Already registered likes " + feed.getFeedId());
            return false;
        }
        likesRepository.save(new Likes(feed, user));
        return true;
    }

    public boolean removeLikesFromFeed(Feed feed, User user) {
        Likes likes = likesRepository.findByFeedAndUser(feed, user);

        if (likes == null) {
            log.info("Likes is not registered");
            return false;
        }

        likesRepository.delete(likes);
        return true;
    }
}
