package QuesMeDemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "COMPLAINT", schema = "public")
public class ComplaintEntity {

    @Id
    @Column(name = "id_complaint")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idComplaint;

    @Column(name = "description", nullable = false, length = 1000)
    private String description;

    @OneToOne(cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_question")
    private QuestionEntity question;

    @Column(name = "accept", nullable = false)
    private Character accept;

    @ManyToOne(cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_admin")
    private AdminEntity admin;


    public ComplaintEntity(String description, QuestionEntity question){
        this.description = description;
        this.question = question;
        this.accept = 'N';
    }
}
