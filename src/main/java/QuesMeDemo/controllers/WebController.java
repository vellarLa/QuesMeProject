package QuesMeDemo.controllers;

import QuesMeDemo.entities.*;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.UserRepository;
import QuesMeDemo.services.*;
import QuesMeDemo.utils.FileUploadUtil;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.patterns.TypePatternQuestions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {
    private static List<QuestionEntity> questions = new ArrayList<QuestionEntity>();
    private static int idUser;
    private static int idQuestion = 1;

    @Autowired
    private final UserService userService;
    private final AdminService adminService;
    private final NotificationService notificationService;
    private final SubscriptionsService subscriptionsService;
    private final ComplaintService complaintService;
    private final QuestionService questionService;
    private final CategoryService categoryService;


    @Value("${welcome.message}")
    private String message;

    @Value("${error.message}")
    private String errorMessage;

    @RequestMapping(value = { "/" }, method = RequestMethod.GET)
    public String welcome(Model model) throws NullFieldException, ErrorFieldException {
        UserEntity welcome_form = new UserEntity();
        model.addAttribute("welcome_form", welcome_form);
        return "welcomepage";
    }

    @RequestMapping(value = { "/welcomeForm" }, method = RequestMethod.GET)
    public String welcomeForm(@ModelAttribute(value="welcome_form") UserEntity welcome_form) {
        List<UserEntity> all = userService.getAll();
        for (UserEntity entity : all) {
            if (welcome_form.getLogin().equals(entity.getLogin()) && welcome_form.getPassword().equals(entity.getPassword())){
                idUser = entity.getIdUser();
                return "redirect:/question";
            }
        }
        return "wrong_log_pas";
    }

    @RequestMapping(value = { "/registration" }, method = RequestMethod.GET)
    public String registration(Model model) throws NullFieldException, ErrorFieldException {
        UserEntity user = new UserEntity();
        model.addAttribute("reg_form", user);
        return "registration";
    }
    @RequestMapping(value = { "/question" }, method = RequestMethod.GET)
    public String question(Model model) {
        QuestionEntity question_form = new QuestionEntity();
        model.addAttribute("question_form", question_form);
        List<String> options = new ArrayList<String>();
        for (CategoryEntity entity : categoryService.getAll()) {
            options.add(entity.getTitle());
        }
        model.addAttribute("options", options);

        List<String> receivers = new ArrayList<String>();
        for (UserEntity entity : userService.getAll()){
            if (entity.getIdUser().equals(idUser))
                continue;
            receivers.add(entity.getNickname());
            //System.out.println(entity);
        }
        model.addAttribute("receivers", receivers);
        for (String a : receivers) {
            System.out.println(a);
        }
        return "question";
    }
    @RequestMapping(value = { "/processFormSave" }, method = RequestMethod.GET)
    public String processFormSave(@ModelAttribute(value="question_form") QuestionEntity ques) throws NullFieldException, ErrorFieldException {
        for (UserEntity entity : userService.getAll()){
            if (entity.getNickname().equals(ques.getReceiver().getNickname()))
            {
                ques.setReceiver(entity);
                break;
            }
        }
        for (CategoryEntity category : categoryService.getAll()){
            if (category.getTitle().equals(ques.getCategory().getTitle()))
            {
                ques.setCategory(category);
                break;
            }
        }
        UserEntity current_user = userService.getById(idUser).get();
        questionService.save(new QuestionEntity(current_user, ques.getReceiver(), ques.getCategory(), ques.getText(), ques.getAnonymous()));
        return "redirect:/complaint";
    }
    @RequestMapping(value = "/registrationPost", method = RequestMethod.POST)
    public String registrationPost(@ModelAttribute("user") UserEntity user, Model model, @RequestParam("photo") MultipartFile multipartFile) throws NullFieldException, ErrorFieldException, IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) userService.updateAvatar(user, fileName);
        user.updateAvatarImagePath();
        userService.save(user);
        idUser = user.getIdUser();
        if (!fileName.isEmpty()) {
            String uploadDir = "user-photos/" + user.getIdUser();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/profile";
        //return new RedirectView("/temp", true);
    }
    @RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
    public String profile(Model model) throws NullFieldException, ErrorFieldException {
        UserEntity user = userService.getById(1).get();
        String nameUser ="Иван Иванов";
        String description = "Люблю читать, петь и изучать языки.";
        String subNum = "15";
        String folNum = "10";
        String ans = "5";
        String ques = "7";
        /*for (QuestionEntity question : questions) {
            question.meLikedBool(user);
        }*/
        questions = questionService.getAll();
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.avatarImagePath);
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questions);
        model.addAttribute("user", user);
        return "main_profile";
    }
    @RequestMapping(value = { "/updateAvatar" }, method=RequestMethod.POST)
    public String updateAvatarProfile(@RequestParam("photo") MultipartFile multipartFile, Model model) throws IOException, NullFieldException, ErrorFieldException {
        UserEntity user = userService.getById(idUser).get();
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) userService.updateAvatar(user, fileName);
        user.updateAvatarImagePath();
        userService.save(user);
        if (!fileName.isEmpty()) {
            String uploadDir = "user-photos/" + user.getIdUser();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/profile";
    }
    @RequestMapping(value = { "/profile/{idQuestion}" }, method = RequestMethod.GET)
    public String like (@PathVariable(name = "idQuestion") int idQuestion,
                       RedirectAttributes redirectAttributes,
                       @RequestHeader(required = false) String referer) throws NullFieldException, ErrorFieldException {
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
    @RequestMapping(value = { "/profile/edit" }, method = RequestMethod.GET)
    public String editProfile (Model model) throws NullFieldException, ErrorFieldException {
        boolean flag = false;
        UserEntity user = userService.getById(idUser).get();
        if (user.getSex() == 'W') flag = true;
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        return "editProfile";
    }
    @RequestMapping(value = { "/profileAdmin/edit" }, method = RequestMethod.GET)
    public String editProfileAdmin (Model model) throws NullFieldException, ErrorFieldException {
        AdminEntity admin = adminService.getById(1).get();
        model.addAttribute("user", admin);
        model.addAttribute("login", admin.getLogin());
        return "editProfileAdmin";
    }
    @RequestMapping(value = { "/profile/followers" }, method = RequestMethod.GET)
    public String myFollowers (Model model) throws NullFieldException, ErrorFieldException {
        UserEntity user = userService.getById(idUser).get();
        //List<UserEntity> followers = subscriptionsService.Followers(idUser);
        List<UserEntity> followers = userService.getAll();
        model.addAttribute("followers", followers);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.avatarImagePath);
        return "followers";
    }
    @RequestMapping(value = { "/profile/subscriptions" }, method = RequestMethod.GET)
    public String mySubscriptions (Model model) throws NullFieldException, ErrorFieldException {
        UserEntity user = userService.getById(idUser).get();
        //List<UserEntity> followers = subscriptionsService.MySubscriptions(idUser);
        List<UserEntity> followers = userService.getAll();
        model.addAttribute("followers", followers);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.avatarImagePath);
        return "subscriptions";
    }
    @RequestMapping(value = { "/profileAdmin" }, method = RequestMethod.GET)
    public String adminProfile (Model model) throws NullFieldException, ErrorFieldException {
        AdminEntity admin = adminService.getById(1).get();
        CategoryEntity category = new CategoryEntity();
        model.addAttribute("FullName", admin.getFullName());
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryService.getAll());
        return "admin_profile";
    }
    @RequestMapping(value = {"/forget_password" }, method = RequestMethod.GET)
    public String forget_password(Model model) {

        UserEntity fog_form = new UserEntity();
        model.addAttribute("fog_form", fog_form);
        return "forget_password";
    }
    @RequestMapping(value = { "/forgetForm" }, method = RequestMethod.GET)
    public String forgetForm(@ModelAttribute(value="fog_form") UserEntity fog_form) throws NullFieldException, ErrorFieldException {
        List<UserEntity> all = userService.getAll();
        for (UserEntity entity : all) {
            if (fog_form.getLogin().equals(entity.getLogin()) && fog_form.getNickname().equals(entity.getNickname()) && fog_form.getName().equals(entity.getName())) {
                System.out.println(entity);
                userService.updatePasswordById(entity.getIdUser(), fog_form.getPassword());
                System.out.println(entity);
                return "redirect:/welcomepage";
            }
        }
        return "wrong_log_pas";
    }
    @RequestMapping(value = { "/welcomeFormBack" }, method = RequestMethod.GET)
    public String welcomeFormBack() {
        return "redirect:/welcomepage";
        //return "redirect:/complaint";
    }
    @RequestMapping(value = {"/notification" }, method = RequestMethod.GET)
    public String notification(Model model) {
        List<NotificationEntity> notification = new ArrayList<>();
        UserEntity current_user = userService.getById(idUser).get();
        for (NotificationEntity entity : notificationService.getAll()){
            if (entity.getOwner().equals(current_user)){
                notification.add(entity);
            }
        }
        model.addAttribute("notifications", notification);
        return "notification";
    }
    @RequestMapping(value="notifications/doDelete/{idNotification}", method = RequestMethod.GET)
    public String deleteNotification (@PathVariable int idNotification) {
        notificationService.delById(idNotification);
        return "redirect:/notification";
    }
    @RequestMapping(value = {"/complaint" }, method = RequestMethod.GET)
    public String complaint(Model model) {
        //String textt = question_form.getText();
        //model.addAttribute("textt", textt);
        QuestionEntity question_form = questionService.getById(idQuestion).get();
        ComplaintEntity complaint_form = new ComplaintEntity("", question_form);
        model.addAttribute("complaint_form", complaint_form);
        return "complaint";
    }
    @RequestMapping(value = { "/complaintSave" }, method = RequestMethod.GET)
    public String complaintSave(@ModelAttribute(value="complaint_form") ComplaintEntity complaint_form) throws NullFieldException, ErrorFieldException {
        QuestionEntity question_form = questionService.getById(idQuestion).get();
        complaintService.save(new ComplaintEntity(complaint_form.getDescription(), question_form));
        return "redirect:/admin_list_complaint";
    }
    @RequestMapping(value = {"/admin_list_complaint"}, method = RequestMethod.GET)
    public String admin_list_complaint(Model model) {
        List<ComplaintEntity> complaint = new ArrayList<>();
        for (ComplaintEntity entity : complaintService.getAll()){
            if (entity.getStatus().equals("Рассматривается")){
                complaint.add(entity);
            }
        }
        model.addAttribute("complaints", complaint);
        return "admin_list_complaint";
    }
    @RequestMapping(value="complaints/consider/{idComplaint}", method = RequestMethod.GET)
    public String considerComplaint (@PathVariable int idComplaint, @RequestParam(value="action", required=true) String action) throws NullFieldException, ErrorFieldException {
        System.out.println(complaintService.getById(idComplaint));

        if (action.equals("accept")) {
            complaintService.updateStatus(idComplaint, "Принята");
        }
        if (action.equals("reject")) {
            complaintService.updateStatus(idComplaint, "Отклонена");
        }
        System.out.println(complaintService.getById(idComplaint));
        return "redirect:/admin_list_complaint";
    }
}
