package QuesMeDemo.repositories;

import QuesMeDemo.entities.SubscriptionsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionsRepository extends JpaRepository<SubscriptionsEntity,Integer>{
}
