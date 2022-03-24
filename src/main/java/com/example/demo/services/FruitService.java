package com.example.demo.services;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.entities.FruitEntity;
import com.example.demo.repositories.FruitRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor//аннотация от Ломбок
public class FruitService {

    private final FruitRepository fruitRepository;

//здесь когда то был конструктор

    public Optional<FruitEntity> getById(Integer id) {
        return fruitRepository.findById(id);
    }

    public void delById(Integer id) {
        fruitRepository.deleteById(id);
    }

    public void save(FruitEntity fruitEntity) {
        fruitRepository.save(fruitEntity);
    }

    public List<FruitEntity> getAll() {
        return fruitRepository.findAll();
    }

    public void saveAll(List<FruitEntity> fruits) {
        fruitRepository.saveAll(fruits);
    }
}