package com.example.demo.services;

import com.example.demo.entities.UserEntity;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.entities.QuestionEntity;
import com.example.demo.repositories.QuestionRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

//здесь когда то был конструктор

    public Optional<UserEntity> getById(Integer idQuestion) {
        return userRepository.findById(idQuestion);
    }

    public void delById(Integer idQuestion) {
        userRepository.deleteById(idQuestion);
    }

    public void save(UserEntity questionEntity) {
        userRepository.save(questionEntity);
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public void saveAll(List<UserEntity> questions) {
        userRepository.saveAll(questions);
    }
}
