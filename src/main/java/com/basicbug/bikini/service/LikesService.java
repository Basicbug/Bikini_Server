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

    public Likes addLikesToFeed(Feed feed, User user) {
        Likes oldLikes = likesRepository.findByFeedAndUser(feed, user);

        if (oldLikes != null) {
            log.info("Already registered likes " + feed.getFeedId());
            return oldLikes;
        }

        Likes newLikes = new Likes();
        newLikes.setFeed(feed);
        newLikes.setUser(user);

        likesRepository.save(newLikes);
        return newLikes;
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

    public Likes getLikesForFeedByUser(Feed feed, User user) {
        return likesRepository.findByFeedAndUser(feed, user);
    }
}
