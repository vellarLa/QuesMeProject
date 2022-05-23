package QuesMeDemo.entities;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Data
@Entity
@AllArgsConstructor
@Table(name = "USERS")
public class UserEntity {

    @Id
    @Column(name = "id_user")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idUser;

    @Column(name = "avatar")
    private String avatar;

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

    public String avatarImagePath;


    /*@Transient
    public String getAvatarImagePath() {
        if (avatar == null || idUser == null) return null;

        return "/src/main/resources/static//user-photos/" + idUser + "/" + avatar;
    }*/

    public UserEntity() {
        this.active = 'А';
        this.ban = 0;
        this.sex = 'M';
    }

    public UserEntity (String nickname, String name, String description, Character sex, String login, String password, String avatar) {
        this.nickname = nickname;
        this.name = name;
        this.description = description;
        if (sex != null) this.sex = sex;
        else this.sex = 'M';
        this.login = login;
        this.password = password;
        this.active = 'А';
        this.ban = 0;
        if (avatar != null) {
            this.avatar = avatar;
        }
    }
    public void updateAvatarImagePath() {
        if (avatar != null && idUser != null)
            avatar = "/user-photos/" + idUser + "/" + avatar;
    }
    public String makeAvatarImagePath(String fileName) {
        return  "/user-photos/" + idUser + "/" + fileName;
    }
}

