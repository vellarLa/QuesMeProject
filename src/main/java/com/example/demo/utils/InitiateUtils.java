package com.example.demo.utils;

import com.example.demo.entities.AdminEntity;
import com.example.demo.entities.CategoryEntity;
import com.example.demo.entities.ComplaintEntity;
import com.example.demo.entities.QuestionEntity;
import com.example.demo.entities.SubscriptionsEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.services.AdminService;
import com.example.demo.services.CategoryService;
import com.example.demo.services.ComplaintService;
import com.example.demo.services.QuestionService;
import com.example.demo.services.SubscriptionsService;
import com.example.demo.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import javax.websocket.Session;
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
        AdminEntity newAdmin = new AdminEntity();
        CategoryEntity newCategory = new CategoryEntity();
        ComplaintEntity newComplaint = new ComplaintEntity();
        QuestionEntity newQuestion = new QuestionEntity();
        SubscriptionsEntity newSubscriptions = new SubscriptionsEntity();
        UserEntity newUser = new UserEntity();
        adminService.save(newAdmin);
        categoryService.save(newCategory);
        complaintService.save(newComplaint);
        questionService.save(newQuestion);
        subscriptionsService.save(newSubscriptions);
        userService.save(newUser);

        List<AdminEntity> allAdmins = adminService.getAll();
        List<CategoryEntity> allCategories = categoryService.getAll();
        List<ComplaintEntity> allComplaints = complaintService.getAll();
        List<QuestionEntity> allQuestions = questionService.getAll();
        List<SubscriptionsEntity> allSubscriptions = subscriptionsService.getAll();
        List<UserEntity> allUsers = userService.getAll();

        for (ComplaintEntity entity : allComplaints) {
            System.out.println(entity);
        }
    }
}

