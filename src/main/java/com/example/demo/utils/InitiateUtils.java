package com.example.demo.utils;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.example.demo.entities.FruitEntity;
import com.example.demo.services.FruitService;

import javax.websocket.Session;
import java.util.List;

@Service
public class InitiateUtils implements CommandLineRunner {

    private final FruitService fruitService;
    public InitiateUtils (FruitService fruitService) {//незабываем конструктор для внедрения
        this. fruitService = fruitService;
    }

    @Override
    public void run(String... args) throws Exception {

//создаем несколько сущностей фруктов, через сеттеры заполняем поля
      FruitEntity fruitEntity4 = new FruitEntity();
        fruitEntity4.setFruitName("fruit4");
        fruitEntity4.setProviderCode(4);

        /*FruitEntity fruitEntity2 = new FruitEntity();
        fruitEntity2.setFruitName("fruit2");
        fruitEntity2.setProviderCode(2);

        FruitEntity fruitEntity3 = new FruitEntity();
        fruitEntity3.setFruitName("fruit3");
        fruitEntity3.setProviderCode(3);

//с помощью переменной сервиса вызываем методы сохранения в базу, по разу для одного объекта
        fruitService.save(fruitEntity1);
        fruitService.save(fruitEntity2);*/
        fruitService.save(fruitEntity4);

//здесь вытаскиваем базу обратно
        List<FruitEntity> all = fruitService.getAll();

//и выводим что получилось
        for (FruitEntity entity : all) {
            System.out.println(entity);
        }
    }
}

