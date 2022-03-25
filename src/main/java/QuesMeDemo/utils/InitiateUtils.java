package QuesMeDemo.utils;

import QuesMeDemo.entities.SubscriptionsEntity;
import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.entities.CategoryEntity;
import QuesMeDemo.entities.ComplaintEntity;
import QuesMeDemo.entities.QuestionEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.services.AdminService;
import QuesMeDemo.services.CategoryService;
import QuesMeDemo.services.ComplaintService;
import QuesMeDemo.services.QuestionService;
import QuesMeDemo.services.SubscriptionsService;
import QuesMeDemo.services.UserService;
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
        AdminEntity newAdmin = new AdminEntity("Привет", "lll", "111111");
        CategoryEntity newCategory = new CategoryEntity("Other");
        ComplaintEntity newComplaint = new ComplaintEntity("Кто прочитал тот молодец", 1, 2, 666);
        QuestionEntity newQuestion = new QuestionEntity(7, 1, 4, "Как дела? Как дела? Это новый кадилак");
        SubscriptionsEntity newSubscriptions = new SubscriptionsEntity(111, 222);
        UserEntity newUser = new UserEntity("valla", "Valler", "kkk", 'M',"gggg", "111111");
        adminService.save(newAdmin);
        categoryService.save(newCategory);
        complaintService.save(newComplaint);
        questionService.save(newQuestion);
        subscriptionsService.save(newSubscriptions);
        userService.save(newUser);

        //List<AdminEntity> allAdmins = adminService.getAll();
        List<CategoryEntity> allCategories = categoryService.getAll();
        List<ComplaintEntity> allComplaints = complaintService.getAll();
        List<QuestionEntity> allQuestions = questionService.getAll();
        //List<SubscriptionsEntity> allSubscriptions = subscriptionsService.getAll();
        //List<UserEntity> allUsers = userService.getAll();

        for (ComplaintEntity entity : allComplaints) {
            System.out.println(entity);
        }
    }
}

