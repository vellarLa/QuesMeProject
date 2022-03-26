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
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.hibernate.query.Query;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InitiateUtils implements CommandLineRunner {

    private final AdminService adminService;
    private final CategoryService categoryService;
    private final ComplaintService complaintService;
    private final QuestionService questionService;
    private final SubscriptionsService subscriptionsService;
    private final UserService userService;

    public InitiateUtils (AdminService adminService, CategoryService categoryService, ComplaintService complaintService,
                          QuestionService questionService, SubscriptionsService subscriptionsService,
                          UserService userService){
        this.adminService = adminService;
        this.categoryService = categoryService;
        this.complaintService = complaintService;
        this.questionService = questionService;
        this.subscriptionsService = subscriptionsService;
        this.userService = userService;
    }
    @Override
    public void run(String... args) throws Exception {
        //AdminEntity newAdmin = new AdminEntity("Привет", "lll", "111111");
        CategoryEntity newCategory = new CategoryEntity("Other");
        CategoryEntity newCategory1 = new CategoryEntity("Love");
        UserEntity user1 = new UserEntity("vallarla", "Valler", "kkk", 'Ж',"gggg", "111111");
        UserEntity user2 = new UserEntity("gogolga", "Olga", "ooo", 'Ж',"dddd", "111111");
        UserEntity user3 = new UserEntity("anonim", "Anon", "ppp", 'М',"iiii", "111111");
        QuestionEntity newQuestion = new QuestionEntity(user1, user2, newCategory, "Как дела? Как дела? Это новый кадилак");
        QuestionEntity newQuestion1 = new QuestionEntity(user1, user2, newCategory1, "Как дела? Как дела? Это новый кадилак");
        ComplaintEntity newComplaint = new ComplaintEntity("Кто прочитал тот молодец", newQuestion);
        ComplaintEntity newComplaint1 = new ComplaintEntity("Кто прочитал тот молодец", newQuestion1);

        //newComplaint.setAdmin(newAdmin);
        SubscriptionsEntity newSubscriptions = new SubscriptionsEntity(user1, user2);
        SubscriptionsEntity newSubscriptions1 = new SubscriptionsEntity(user1, user3);
        SubscriptionsEntity newSubscriptions2 = new SubscriptionsEntity(user3, user1);
        try {
            userService.save(user1);
            userService.save(user2);
            userService.save(user3);
            //subscriptionsService.save(newSubscriptions);
            //subscriptionsService.save(newSubscriptions1);
            //subscriptionsService.save(newSubscriptions2);
            categoryService.save(newCategory);
            categoryService.save(newCategory1);
            questionService.save(newQuestion);
            questionService.save(newQuestion1);
            complaintService.save(newComplaint);
            complaintService.save(newComplaint1);
            categoryService.delById(1);
            //questionService.delById(1);
            //questionService.delById(1);
            //complaintService.delById(1);
            //categoryService.delById(1);
            //userService.delById(1);
            //userService.updateLogin(1, "vallar");
        } catch (NullFieldException | ErrorFieldException ex) {System.out.println(ex.getMessage());}
        //userService.save(user1);
        //user1.setDescription("lll");
        //userService.save(user1);
        //userService.save(user1);
        //userService.save(user2);
        //adminService.save(newAdmin);
        //categoryService.save(newCategory);
        //questionService.save(newQuestion);
        //complaintService.save(newComplaint);
        //subscriptionsService.save(newSubscriptions);
        //List<AdminEntity> all = adminService.getAll();
        //List<CategoryEntity> all = categoryService.getAll();
        //List<ComplaintEntity> all = complaintService.getAll();
        List<QuestionEntity> all = questionService.getAll();
        //List<SubscriptionsEntity> all = subscriptionsService.getAll();
        //List<UserEntity> all = userService.getAll();

        for (QuestionEntity entity : all) {
            System.out.println(entity);
        }
    }
}

