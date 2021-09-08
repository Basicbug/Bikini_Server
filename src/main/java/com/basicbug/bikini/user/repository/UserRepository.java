package com.basicbug.bikini.user.repository;

import com.basicbug.bikini.user.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUid(String uid);
    Optional<User> findByUsername(String username);
}
