package com.basicbug.bikini.service;

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
}
