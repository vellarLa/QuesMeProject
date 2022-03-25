package com.example.demo.entities;
import lombok.Data;
import lombok.NonNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "QUESTION")
public class QuestionEntity {

    @Id
    @Column(name = "id_question")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idQuestion;

    @Column(name = "id_sender")
    private Integer idSender;

    @Column(name = "id_receiver")
    private Integer idReceiver;

    @Column(name = "id_category")
    private Integer idCategory;

    @Column(name = "status")
    private String status;

    @Column(name = "text")
    private String text;

    @Column(name = "anonymous")
    private Character anonymous;

}
