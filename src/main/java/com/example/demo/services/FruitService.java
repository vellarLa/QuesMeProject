package com.example.demo.services;


import org.springframework.stereotype.Service;
import com.example.demo.entities.FruitEntity;
import com.example.demo.repositories.FruitRepository;

import java.util.List;

@Service
public class FruitService {

    private final FruitRepository fruitRepository;

    public FruitService(FruitRepository fruitRepository) {//внедили зависимость
        this.fruitRepository = fruitRepository;
    }

    //создали публичный метод (название любое может быть)
//на вход принимает сущность и сохраняет ее в базу
    public void save(FruitEntity fruitEntity){
        fruitRepository.save(fruitEntity); //реализовали метод внедренного бина
    }

    //возвращает лист всех сущностей из базы
    public List<FruitEntity> getAll(){
        return fruitRepository.findAll(); //реализовали метод внедренного бина
    }
}
