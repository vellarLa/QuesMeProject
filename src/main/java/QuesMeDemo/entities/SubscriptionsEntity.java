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
@Table(name = "SUBSCRIPTIONS")
public class SubscriptionsEntity {

    @Id
    @Column(name = "id_subscriptions")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idSubscriptions;

    @Column(name = "id_user", nullable = false)
    private Integer idUser;

    @Column(name = "id_subscript", nullable = false)
    private Integer idSubscript;

    public SubscriptionsEntity(Integer idUser, Integer idSubscript) {
        this.idUser = idUser;
        this.idSubscript = idSubscript;
    }

}
