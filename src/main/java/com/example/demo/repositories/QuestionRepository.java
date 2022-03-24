package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity,Integer>{
}
