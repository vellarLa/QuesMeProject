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

    @ManyToOne(cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_user")
    private UserEntity user;

    @ManyToOne(cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_subscript")
    private UserEntity subscript;

    public SubscriptionsEntity(UserEntity user, UserEntity subscript) {
        this.user = user;
        this.subscript = subscript;
    }

}
