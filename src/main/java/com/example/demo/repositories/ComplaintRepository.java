package com.example.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.entities.ComplaintEntity;

@Repository
public interface ComplaintRepository extends JpaRepository<ComplaintEntity,Integer>{
}
