package com.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.entities.SubscriptionsEntity;
import com.example.demo.repositories.SubscriptionsRepository;
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

    public void save(SubscriptionsEntity subscriptionsEntity) {
        subscriptionsRepository.save(subscriptionsEntity);
    }

    public List<SubscriptionsEntity> getAll() {
        return subscriptionsRepository.findAll();
    }

    public void saveAll(List<SubscriptionsEntity> subscriptions) {
        subscriptionsRepository.saveAll(subscriptions);
    }
}
