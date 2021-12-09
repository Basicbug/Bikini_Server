package com.basicbug.bikini.user.dto;

import com.basicbug.bikini.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequestDto {
    private String username;

    public User toUserEntity() {
        return User.builder()
            .username(this.username)
            .build();
    }
}
