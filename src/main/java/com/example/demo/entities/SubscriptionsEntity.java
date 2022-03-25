package com.example.demo.entities;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "SUBSCRIPTIONS")
public class SubscriptionsEntity {

    @Id
    @Column(name = "id_user")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer idUser;

    @Column(name = "id_subscript")
    private Integer idSubscript;

}
