package com.example.demo.entities;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Data//аннотация сгенирует при компиляции необходимый код
@Entity
@Table(name = "USERS")
public class UserEntity {

    @Id
    @Column(name = "id_user0")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idUser;
    /*
    @NonNull
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
    @Column(name = "nickname0")
    private String nickname0;

    @Column(name = "name0")
    private String name0;

    @Column(name = "description0")
    private String description0;

    @Column(name = "sex0")
    private Character sex0;

    @Column(name = "active0")
    private Character active0;

    @Column(name = "ban0")
    private Integer ban0;

    @Column(name = "login0")
    private String login0;

    @Column(name = "password0")
    private String password0;
}

