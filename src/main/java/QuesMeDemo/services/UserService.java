package QuesMeDemo.services;

import QuesMeDemo.repositories.UserRepository;
import QuesMeDemo.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserEntity> getById(Integer idUser) {
        return userRepository.findById(idUser);
    }

    public void delById(Integer idUser) {
        userRepository.deleteById(idUser);
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public void saveAll(List<UserEntity> questions) {
        userRepository.saveAll(questions);
    }
}
