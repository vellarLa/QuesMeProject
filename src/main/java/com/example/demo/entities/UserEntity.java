package com.example.demo.entities;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data//аннотация сгенирует при компиляции необходимый код
@Entity
@Table(name = "fruit_table")
public class UserEntity {

    @Id
    @Column(name = "id_fruit")
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private Integer id;

    @Column(name = "fruit_name")
    private String fruitName;

    @Column(name = "provider_code")
    private Integer providerCode;
}

