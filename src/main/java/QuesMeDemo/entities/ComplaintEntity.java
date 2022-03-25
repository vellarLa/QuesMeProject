package QuesMeDemo.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @Column(name = "id_sender", nullable = false)
    private Integer idSender;

    @Column(name = "id_hater", nullable = false)
    private Integer idCategory;

    @Column(name = "id_question", nullable = false)
    private Integer idQuestion;

    @Column(name = "accept", nullable = false, length = 1)
    private Character accept;

    @Column(name = "id_admin")
    private Integer idAdmin;

    public ComplaintEntity(String description, Integer idSender, Integer idCategory, Integer idQuestion){
        this.description = description;
        this.idSender = idSender;
        this.idCategory = idCategory;
        this.idQuestion = idQuestion;
        this.accept = 'N';
    }
}
