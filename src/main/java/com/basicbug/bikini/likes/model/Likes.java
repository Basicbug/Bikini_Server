package com.basicbug.bikini.likes.model;

import com.basicbug.bikini.common.model.BaseEntity;
import com.basicbug.bikini.likes.type.TargetType;
import com.basicbug.bikini.user.model.User;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Likes extends BaseEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    @Column(columnDefinition = "VARCHAR(36)", nullable = false)
    @Type(type = "org.hibernate.type.UUIDCharType")
    private UUID id;

    private TargetType targetType;

    private String targetId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Likes(TargetType targetType, String targetId, User user) {
        this.targetType = targetType;
        this.targetId = targetId;
        this.user = user;
    }
}
