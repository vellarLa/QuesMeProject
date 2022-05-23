package QuesMeDemo.services;

import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.entities.SubscriptionsEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.repositories.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
                throw new ErrorFieldException("Вы уже подписаны на этого пользователя.");
            }
        }
        subscriptionsRepository.save(subscriptionsEntity);
    }
    public List<SubscriptionsEntity> getAll() {
        return subscriptionsRepository.findAll();
    }

    public List<UserEntity> Followers (int idUser) {
        List<UserEntity> followers = new ArrayList<UserEntity>();
        List<SubscriptionsEntity> all = getAll();
        for (SubscriptionsEntity subscript : all) {
            if (subscript.getSubscript().getIdUser() == idUser) {
                followers.add(subscript.getUser());
            }
        }
        return followers;
    }

    public List<UserEntity> MySubscriptions (int idUser) {
        List<UserEntity> subscriptions = new ArrayList<UserEntity>();
        List<SubscriptionsEntity> all = getAll();
        for (SubscriptionsEntity subscript : all) {
            if (subscript.getUser().getIdUser() == idUser) {
                subscriptions.add(subscript.getSubscript());
            }
        }
        return subscriptions;
    }
    public int FollowersNum (int idUser) {
        List<UserEntity> followers = new ArrayList<UserEntity>();
        int num = 0;
        List<SubscriptionsEntity> all = getAll();
        for (SubscriptionsEntity subscript : all) {
            if (subscript.getSubscript().getIdUser() == idUser) {
                num++;
            }
        }
        return num;
    }

    public int MySubscriptionsNum (int idUser) {
        List<UserEntity> subscriptions = new ArrayList<UserEntity>();
        int num = 0;
        List<SubscriptionsEntity> all = getAll();
        for (SubscriptionsEntity subscript : all) {
            if (subscript.getUser().getIdUser() == idUser) {
                num++;
            }
        }
        return num;
    }
    public boolean isMySubscription (Integer idUser, Integer idGuest) {
        List<UserEntity> all = MySubscriptions(idUser);
        for (UserEntity subscript : all) {
            if (subscript.getIdUser() == idGuest) {
                return true;
            }
        }
        return false;
    }
    /*public void saveAll(List<SubscriptionsEntity> subscriptions) {
        subscriptionsRepository.saveAll(subscriptions);
    }*/
    public void delSubscript (Integer idUser, Integer idSubscript) {
        List<SubscriptionsEntity> all = getAll();
        for (SubscriptionsEntity subscript : all) {
            if (subscript.getUser().getIdUser() == idUser && subscript.getSubscript().getIdUser() == idSubscript) {
                delById(subscript.getIdSubscriptions());
            }
        }
    }
    public List<UserEntity> makeCondition(List<UserEntity> all, Integer idUser) {
        for (UserEntity subscript : all) {
            if (isMySubscription(idUser, subscript.getIdUser())) {
                subscript.setSubscript(true);
            }
        }
        return all;
    }
}
