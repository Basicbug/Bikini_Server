package com.basicbug.bikini.likes.service;

import com.basicbug.bikini.user.model.User;
import com.basicbug.bikini.auth.exception.UserNotFoundException;
import com.basicbug.bikini.likes.model.Likes;
import com.basicbug.bikini.likes.type.TargetType;
import com.basicbug.bikini.likes.repository.LikesRepository;
import com.basicbug.bikini.user.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikesService {

    private final LikesRepository likesRepository;
    private final UserRepository userRepository;

    public List<String> getMostLikesTargetIds(TargetType targetType, int limit) {
        return likesRepository.getMostLikesForTarget(targetType, limit);
    }

    public Likes addLikesToTarget(TargetType targetType, String targetId, String uid) {
        User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException("Invalid user : " + uid));

        Likes oldLikes = likesRepository.findByTargetTypeAndTargetIdAndUser(targetType, targetId, user);

        if (oldLikes != null) {
            log.info("Already registered likes : " + targetType + " " + targetId);
            return oldLikes;
        }

        Likes newLikes = new Likes(targetType, targetId, user);

        likesRepository.save(newLikes);
        return newLikes;
    }

    public boolean removeLikesFromTarget(TargetType targetType, String targetId, String uid) {
        User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException("Invalid user : " + uid));

        Likes likes = likesRepository.findByTargetTypeAndTargetIdAndUser(targetType, targetId, user);

        if (likes == null) {
            log.info("Likes is not registered");
            return false;
        }

        likesRepository.delete(likes);
        return true;
    }

    public Likes getLikesForTargetByUser(TargetType targetType, String targetId, String uid) {
        User user = userRepository.findByUid(uid).orElseThrow(() -> new UserNotFoundException("Invalid user : " + uid));
        return likesRepository.findByTargetTypeAndTargetIdAndUser(targetType, targetId, user);
    }

    public int getLikesCount(TargetType targetType, String targetId) {
        return likesRepository.findAllByTargetTypeAndTargetId(targetType, targetId).size();
    }
}
