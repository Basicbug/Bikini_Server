package com.basicbug.bikini.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@Entity
public class User {

    public User() {}

    public User(@Email @Size(max = 40) String email, @NotBlank @Size(max = 100) String password, @NotNull String userRole) {
        this.email = email;
        this.password = password;
        this.userRole = userRole;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Size(max = 40)
    private String email;

    @NotBlank
    @Size(max = 100)
    private String password;

    private String userRole;
}
