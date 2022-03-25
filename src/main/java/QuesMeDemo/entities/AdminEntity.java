package QuesMeDemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ADMINS")
public class AdminEntity {

    @Id
    @Column(name = "id_admin")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idAdmin;

    @Column(name = "full_name", nullable = false, length = 50)
    private String fullName;

    @Column(name = "login", nullable = false, unique = true, length = 15)
    private String login;

    @Column(name = "password", nullable = false, length = 15)
    private String password;

    public AdminEntity (String fullName, String login, String password) {
        this.fullName = fullName;
        this.login = login;
        this.password = password;
    }
}
