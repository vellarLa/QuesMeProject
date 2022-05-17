package QuesMeDemo.services;

import QuesMeDemo.entities.CategoryEntity;
import QuesMeDemo.entities.NotificationEntity;
import QuesMeDemo.entities.QuestionEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Optional<NotificationEntity> getById(Integer idNotification) {
        return notificationRepository.findById(idNotification);
    }

    public void delById(Integer idNotification) {
        notificationRepository.deleteById(idNotification);
    }

    public void save(NotificationEntity notificationEntity) throws NullFieldException, ErrorFieldException {

        notificationRepository.save(notificationEntity);
    }

    public List<NotificationEntity> getAll() {
        return notificationRepository.findAll();
    }

    public void saveAll(List<NotificationEntity> notifications) {
        notificationRepository.saveAll(notifications);
    }

}
