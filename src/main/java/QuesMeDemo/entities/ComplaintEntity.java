package QuesMeDemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

    @Column(name = "description", length = 1000)
    private String description;

    @OneToOne(cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_question")
    private QuestionEntity question;

    @Column(name = "status")
    private String status;

    @ManyToOne(cascade=CascadeType.MERGE)
    @PrimaryKeyJoinColumn(name = "id_admin")
    private AdminEntity admin;


    public ComplaintEntity(String description, QuestionEntity question){
        this.description = description;
        this.question = question;
        this.status = "Рассматривается";
    }
}
