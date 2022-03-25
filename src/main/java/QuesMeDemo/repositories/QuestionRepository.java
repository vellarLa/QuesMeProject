package QuesMeDemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import QuesMeDemo.entities.QuestionEntity;

@Repository
public interface QuestionRepository extends JpaRepository<QuestionEntity,Integer>{
}
