package com.example.demo.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "COMPLAINT", schema = "public")
public class ComplaintEntity {

    @Id
    @Column(name = "id_complaint")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idComplaint;

    @Column(name = "description")
    private String description;

    @Column(name = "id_sender")
    private Integer idSender;

    @Column(name = "id_hater")
    private Integer idCategory;

    @Column(name = "id_question")
    private Integer idQuestion;

    @Column(name = "accept")
    private Character accept;

    @Column(name = "id_admin")
    private Integer idAdmin;

}
