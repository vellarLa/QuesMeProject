package com.example.demo.utils;

import com.example.demo.entities.FruitEntity;
import com.example.demo.entities.QuestionEntity;
import com.example.demo.services.QuestionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import com.example.demo.services.FruitService;

import javax.websocket.Session;
import java.util.List;

@Service
public class InitiateUtils implements CommandLineRunner {

    private final QuestionService questionService;
    public InitiateUtils (QuestionService questionService) {//незабываем конструктор для внедрения
        this.questionService = questionService;
    }

    @Override
    public void run(String... args) throws Exception {

        QuestionEntity questionEntity = new QuestionEntity();

        questionEntity.setIdSender(1);
        questionEntity.setIdReceiver(2);
        questionEntity.setIdQuestion(1);
        questionEntity.setText("What is your name?");
        questionEntity.setAnonymous('Y');

        questionService.save(questionEntity);

        List<QuestionEntity> all = questionService.getAll();

//и выводим что получилось
        for (QuestionEntity entity : all) {
            System.out.println(entity);
        }

//создаем несколько сущностей фруктов, через сеттеры заполняем поля
        //FruitEntity fruitEntity4 = new FruitEntity();
        //fruitEntity4.setFruitName("fruit4");
        //fruitEntity4.setProviderCode(4);

        /*FruitEntity fruitEntity2 = new FruitEntity();
        fruitEntity2.setFruitName("fruit2");
        fruitEntity2.setProviderCode(2);

        FruitEntity fruitEntity3 = new FruitEntity();
        fruitEntity3.setFruitName("fruit3");
        fruitEntity3.setProviderCode(3);

//с помощью переменной сервиса вызываем методы сохранения в базу, по разу для одного объекта
        fruitService.save(fruitEntity1);
        fruitService.save(fruitEntity2);*/
        //fruitService.save(fruitEntity4);

//здесь вытаскиваем базу обратно
        /*List<FruitEntity> all = fruitService.getAll();

//и выводим что получилось
        for (FruitEntity entity : all) {
            System.out.println(entity);
        }*/

    }
}

