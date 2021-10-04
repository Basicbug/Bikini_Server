package com.basicbug.bikini.auth.model;

import com.basicbug.bikini.common.model.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken extends BaseEntity {

    @Id
    String key;

    String token;

    Long expireTime;

    public RefreshToken updateToken(String token) {
        this.token = token;
        return this;
    }
}
