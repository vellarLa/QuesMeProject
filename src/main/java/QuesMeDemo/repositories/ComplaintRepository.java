package QuesMeDemo.repositories;

import QuesMeDemo.entities.ComplaintEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity,Integer>{
}
