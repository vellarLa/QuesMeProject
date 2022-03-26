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
        //CategoryEntity newCategory = new CategoryEntity("Other");
        UserEntity user1 = new UserEntity("gogolga", "Valler", "kkk", 'Ж',"gggg", "111111");
        UserEntity user2 = new UserEntity("gogolga", "Olga", "ooo", 'Ж',"dddd", "111111");
        //QuestionEntity newQuestion = new QuestionEntity(sender, receiver, newCategory, "Как дела? Как дела? Это новый кадилак");
        //ComplaintEntity newComplaint = new ComplaintEntity("Кто прочитал тот молодец", newQuestion);
        //newComplaint.setAdmin(newAdmin);
        //SubscriptionsEntity newSubscriptions = new SubscriptionsEntity(user1, user2);
        try {
            userService.save(user1);
            System.out.println("USER1 добавлен");
            userService.save(user2);
            System.out.println("USER2 добавлен");
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
        //List<QuestionEntity> all = questionService.getAll();
        //List<SubscriptionsEntity> all = subscriptionsService.getAll();
        List<UserEntity> all = userService.getAll();

        for (UserEntity entity : all) {
            System.out.println(entity);
        }
    }
}

