package QuesMeDemo.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "QUESTION")
public class QuestionEntity {

    @Id
    @Column(name = "id_question")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idQuestion;

    @ManyToOne (cascade=CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "id_sender")
    private UserEntity sender;

    @ManyToOne (cascade=CascadeType.MERGE)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn (name = "id_receiver")
    private UserEntity receiver;

    @ManyToOne (cascade=CascadeType.MERGE)
    @JoinColumn (name = "id_category")
    private CategoryEntity category;

    /*@OneToOne(optional=false, cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_complaint")
    @JoinColumn(name = "id_complaint")
    private ComplaintEntity complaint;*/

    @Column(name = "status")
    private String status;

    @Column(name = "text", length = 1000)
    private String text;

    @Column(name = "anonymous")
    private Character anonymous;

    public QuestionEntity(UserEntity sender, UserEntity receiver, CategoryEntity category, String text, Character anonymous) {
        this.sender = sender;
        this.receiver = receiver;
        this.category = category;
        this.text = text;
        this.status = "SEND";
        if (anonymous != null) this.anonymous = anonymous;
        else this.anonymous = 'N';
    }
}
