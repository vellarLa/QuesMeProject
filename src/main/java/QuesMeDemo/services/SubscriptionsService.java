package QuesMeDemo.services;

import QuesMeDemo.entities.SubscriptionsEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.repositories.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionsService {

    private final SubscriptionsRepository subscriptionsRepository;

    public Optional<SubscriptionsEntity> getById(Integer idSubscriptions) {
        return subscriptionsRepository.findById(idSubscriptions);
    }

    public void delById(Integer idSubscriptions) {
        subscriptionsRepository.deleteById(idSubscriptions);
    }

    public void save(SubscriptionsEntity subscriptionsEntity) throws ErrorFieldException {
        if (subscriptionsEntity.getUser().getIdUser() == subscriptionsEntity.getSubscript().getIdUser())
            throw new ErrorFieldException("Вы не можете подписаться сами на себя.");
        List<SubscriptionsEntity> all = getAll();
        for (SubscriptionsEntity subscript : all) {
            if (subscript.getUser().getIdUser() == subscriptionsEntity.getUser().getIdUser() &&
                    subscript.getSubscript().getIdUser() == subscriptionsEntity.getSubscript().getIdUser()) {
                throw new ErrorFieldException("Логин не уникален.");
            }
        }
        subscriptionsRepository.save(subscriptionsEntity);
    }

    public List<SubscriptionsEntity> getAll() {
        return subscriptionsRepository.findAll();
    }

    public void saveAll(List<SubscriptionsEntity> subscriptions) {
        subscriptionsRepository.saveAll(subscriptions);
    }
}
