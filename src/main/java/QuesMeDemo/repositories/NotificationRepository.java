package QuesMeDemo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import QuesMeDemo.entities.NotificationEntity;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity,Integer>{
}
