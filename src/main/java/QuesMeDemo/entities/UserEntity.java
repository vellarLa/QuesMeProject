package QuesMeDemo.entities;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.image.BufferedImage;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class UserEntity {

    @Id
    @Column(name = "id_user")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idUser;

    //@Column(name = "avatar")
    //private BufferedImage avatar;

    @Column(name = "nickname", nullable = false, length = 15, unique = true)
    private String nickname;

    @Column(name = "name", nullable = false, length = 25)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "sex", nullable = false)
    private Character sex;

    @Column(name = "active")
    private Character active;

    @Column(name = "ban", length = 2)
    private Integer ban;

    @Column(name = "login", nullable = false, length = 15, unique = true)
    private String login;

    @Column(name = "password", nullable = false, length = 15)
    private String password;

    public UserEntity (String nickname, String name, String description, Character sex, String login, String password) {
        this.nickname = nickname;
        this.name = name;
        this.description = description;
        this.sex = sex;
        this.login = login;
        this.password = password;
        this.active = 'А';
        this.ban = 0;
    }

}

