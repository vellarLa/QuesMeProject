package QuesMeDemo.entities;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

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

    @Column(name = "id_sender", nullable = false)
    private Integer idSender;

    @Column(name = "id_receiver", nullable = false)
    private Integer idReceiver;

    @Column(name = "id_category", nullable = false)
    private Integer idCategory;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "text", nullable = false, length = 1000)
    private String text;

    @Column(name = "anonymous", nullable = false, length = 1)
    private Character anonymous;

    public QuestionEntity(Integer idSender, Integer idReceiver, Integer idCategory, String text) {
        this.idSender = idSender;
        this.idReceiver = idReceiver;
        this.idCategory = idCategory;
        this.status = "SEND";
        this.text = text;
        this.anonymous = 'N';
    }
}
