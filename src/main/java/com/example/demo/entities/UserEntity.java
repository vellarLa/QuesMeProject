package com.example.demo.entities;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Data
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @Column(name = "id_user")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idUser;

    /*@NonNull
    @Column(name = "nickname", unique = true, length = 15)
    private String nickname;

    @NonNull
    @Column(name = "name", length = 30)
    private String name;

    //@Column(name = "avatar")
    //private BufferedImage avatar;

    @Column(name = "description")
    private String description;

    @ColumnDefault("М")
    @Column(name = "sex", nullable = false)
    private Character sex;

    @ColumnDefault("А")
    @Column(name = "active", nullable = false)
    private Character active;

    @ColumnDefault("0")
    @Column(name = "ban", length = 1, nullable = false)
    private Integer ban;

    @NonNull
    @Column(name = "login", length = 10, unique = true)
    private String login;

    @NonNull
    @Column(name = "password", length = 20)
    private String password;*/
    @Column(name = "nickname")
    private String nickname;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sex")
    private Character sex;

    @Column(name = "active")
    private Character active;

    @Column(name = "ban")
    private Integer ban;

    @Column(name = "login")
    private String login;

    @Column(name = "password")
    private String password;
}

