package QuesMeDemo.utils;

import QuesMeDemo.entities.SubscriptionsEntity;
import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.entities.CategoryEntity;
import QuesMeDemo.entities.ComplaintEntity;
import QuesMeDemo.entities.QuestionEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.services.AdminService;
import QuesMeDemo.services.CategoryService;
import QuesMeDemo.services.ComplaintService;
import QuesMeDemo.services.QuestionService;
import QuesMeDemo.services.SubscriptionsService;
import QuesMeDemo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InitiateUtils implements CommandLineRunner {

    private final AdminService adminService;
    private final CategoryService categoryService;
    private final ComplaintService complaintService;
    private final QuestionService questionService;
    private final SubscriptionsService subscriptionsService;
    private final UserService userService;


    public void testingSave(UserEntity user1, UserEntity user2) {
        UserEntity notUnique = new UserEntity("Heroku", "Александ", "Люблю читать и жить в кайф", 'М', "alex1980", "111kkk5");
        try {
            userService.save(notUnique);
        } catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        UserEntity nullField = new UserEntity("alex5", "Александ", "Люблю читать и жить в кайф", 'М', null, "111kkk5");
        try {
            userService.save(nullField);
        } catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        UserEntity wrongLength = new UserEntity("alex5", "Александ", "Люблю читать и жить в кайф", 'М', "alex1980", "111kkk555411cfcfcsdcfvvf88821vf");
        try {
            userService.save(wrongLength);
        } catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        SubscriptionsEntity subscript1 = new SubscriptionsEntity(user1, user1);
        try {
            subscriptionsService.save(subscript1);
        } catch (ErrorFieldException ex) {System.out.println(ex.getMessage());}
        System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        SubscriptionsEntity subscript2 = new SubscriptionsEntity(user1, user2);
        try {
            subscriptionsService.save(subscript2);
        } catch (ErrorFieldException ex) {System.out.println(ex.getMessage());}
    }
    public void updateTest (UserEntity user) {
        try {
            userService.addBan(user.getIdUser());
        }catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        try {
            userService.updateActive(user.getIdUser(), false);
        }catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        try {
            userService.updateNickname(user.getIdUser(), "Heroku");
        }catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
    }
    public void deleteTest (ComplaintEntity complaint, QuestionEntity question, UserEntity user, CategoryEntity category) {
        complaintService.deleteByIdQuestion(4);
        questionService.delById(question.getIdQuestion());
        try {
            categoryService.delById(category.getIdCategory());
        }catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        try {
            userService.addBan(user.getIdUser());
            userService.addBan(user.getIdUser());
            userService.addBan(user.getIdUser());
        }catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
    }
    @Override
    public void run(String... args) throws Exception {
        CategoryEntity newCategory1 = new CategoryEntity("Любовь");
        CategoryEntity newCategory2 = new CategoryEntity("Личное");
        CategoryEntity newCategory3 = new CategoryEntity("Интересы");
        CategoryEntity newCategory4 = new CategoryEntity("Другое");
        CategoryEntity newCategory5 = new CategoryEntity("Обо всём");

        categoryService.save(newCategory1);
        categoryService.save(newCategory2);
        categoryService.save(newCategory3);
        categoryService.save(newCategory4);
        categoryService.save(newCategory5);

        UserEntity user1 = new UserEntity("VallarLa", "Valler", "kkk", 'Ж',"gggg", "111111");
        UserEntity user2 = new UserEntity("gogolga", "Olga", "ooo", 'Ж',"dddd", "777777");
        UserEntity user3 = new UserEntity("JavaRush", "Саша", "website", 'М',"JavaRush", "12345678");
        UserEntity user4 = new UserEntity("Heroku", "Hero", "container", 'М',"postgres", "postgresql");
        UserEntity user5 = new UserEntity("Dashkus", "Даша", "6 faculty is the best", 'Ж',"dashkus", "555555");

        userService.save(user1);
        userService.save(user2);
        userService.save(user3);
        userService.save(user4);
        userService.save(user5);

        QuestionEntity newQuestion1 = new QuestionEntity(user2, user1, newCategory5, "Как дела?", 'N');
        QuestionEntity newQuestion2 = new QuestionEntity(user1, user3, newCategory3, "Что лучше выбрать: oracle или postgres?", 'Y');
        QuestionEntity newQuestion3 = new QuestionEntity(user2, user4, newCategory4, "Как подключиться к Heroku?", 'N');
        QuestionEntity newQuestion4 = new QuestionEntity(user2, user5, newCategory2, "Какие планы на завтра?", 'Y');
        QuestionEntity newQuestion5 = new QuestionEntity(user5, user1, newCategory3, "Какой фильм посмотреть вечером?", 'N');

        questionService.save(newQuestion1);
        questionService.save(newQuestion2);
        questionService.save(newQuestion3);
        questionService.save(newQuestion4);
        questionService.save(newQuestion5);

        ComplaintEntity newComplaint1 = new ComplaintEntity("Слишком личная информация", newQuestion1);
        ComplaintEntity newComplaint2 = new ComplaintEntity("Неприемлемый вопрос для всеобщего обозрения", newQuestion4);
        ComplaintEntity newComplaint3 = new ComplaintEntity("Некорректно составленный вопрос", newQuestion3);

        complaintService.save(newComplaint1);
        complaintService.save(newComplaint2);
        complaintService.save(newComplaint3);

        SubscriptionsEntity newSubscription1 = new SubscriptionsEntity(user1, user2);
        SubscriptionsEntity newSubscription2 = new SubscriptionsEntity(user2, user1);
        SubscriptionsEntity newSubscription3 = new SubscriptionsEntity(user3, user4);
        SubscriptionsEntity newSubscription4 = new SubscriptionsEntity(user1, user5);
        SubscriptionsEntity newSubscription5 = new SubscriptionsEntity(user2, user3);
4
        subscriptionsService.save(newSubscription1);
        subscriptionsService.save(newSubscription2);
        subscriptionsService.save(newSubscription3);
        subscriptionsService.save(newSubscription4);
        subscriptionsService.save(newSubscription5);

        AdminEntity newAdmin1 = new AdminEntity("QuesMe", "Global Admin", "87654321");
        AdminEntity newAdmin2 = new AdminEntity("IntelliJ IDEA", "IDE2021", "202132");
        AdminEntity newAdmin3 = new AdminEntity("Windows", "Win32", "111111");

        adminService.save(newAdmin1);
        adminService.save(newAdmin2);
        adminService.save(newAdmin3);

        //testingSave(user1, user2);
        deleteTest(newComplaint1, newQuestion1, user3, newCategory3);
        //complaintService.deleteByIdQuestion(4);

        //updateTest(user1);
        //List<AdminEntity> all = adminService.getAll();
        //List<CategoryEntity> all = categoryService.getAll();
        List<ComplaintEntity> all = complaintService.getAll();
        //List<QuestionEntity> all = questionService.getAll();
        //List<SubscriptionsEntity> all = subscriptionsService.getAll();
        //List<UserEntity> all = userService.getAll();
         for (ComplaintEntity entity : all) {
            System.out.println(entity);
        }
    }
}

