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

import static org.hibernate.sql.InFragment.NULL;

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
                return "redirect:/profile";
            }
        }
        List<AdminEntity> allAdmin = adminService.getAll();
        for (AdminEntity entity : allAdmin) {
            if (welcome_form.getLogin().equals(entity.getLogin()) && welcome_form.getPassword().equals(entity.getPassword())){
                idUser = entity.getIdAdmin();
                System.out.println(idUser);
                return "redirect:/profileAdmin";
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
        return "redirect:/profile";
    }
    @RequestMapping(value = "/registrationPost", method = RequestMethod.POST)
    public String registrationPost(@ModelAttribute("user") UserEntity user, Model model, @RequestParam("photo") MultipartFile multipartFile) throws NullFieldException, ErrorFieldException, IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) {
            userService.updateAvatar(user, user.makeAvatarImagePath(fileName));
        }
        else {
            userService.updateAvatar(user, "/img/avatar0.jpg");
        }
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
        UserEntity user = userService.getById(idUser).get();
        /*for (QuestionEntity question : questions) {
            question.meLikedBool(user);
        }*/
        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questions = questionService.getReceivedQuestionsAnswer(idUser);
        if (questions.size() == 0) flag = false;
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questions);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        return "main_profile";
    }
    @RequestMapping(value = { "/updateAvatar" }, method=RequestMethod.POST)
    public String updateAvatarProfile(@ModelAttribute("user") UserEntity user, @RequestParam("photo") MultipartFile multipartFile) throws IOException, NullFieldException, ErrorFieldException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        if (!fileName.isEmpty()) {
            userService.updateAvatar(user, fileName);
            user.updateAvatarImagePath();
        }
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
    @RequestMapping(value = { "/answer/{idQuestionAns}" }, method = RequestMethod.GET)
    public String answerQuestion (@PathVariable(name = "idQuestionAns") int idQuestionAns, Model model) throws NullFieldException, ErrorFieldException {
        QuestionEntity question = questionService.getById(idQuestionAns).get();
        idQuestion = idQuestionAns;
        model.addAttribute("question",question);
        return "answerQuestion";
    }
    @RequestMapping(value = { "/deleteQuestion/{idQuestionAns}" }, method = RequestMethod.GET)
    public String deleteQuestion (@PathVariable(name = "idQuestionDel") int idQuestionDel, Model model) throws NullFieldException, ErrorFieldException {
        questionService.delById(idQuestionDel);
        return "redirect:/profile/newQuestions";
    }
    @RequestMapping(value = {"/complaint/{idQuestionCom}" }, method = RequestMethod.GET)
    public String complaint(@PathVariable(name = "idQuestionCom") int idQuestionCom,Model model) throws NullFieldException, ErrorFieldException {
        QuestionEntity question_form = questionService.getById(idQuestionCom).get();
        questionService.updateStatus(idQuestionCom, "Получена жалоба");
        ComplaintEntity complaint_form = new ComplaintEntity("", question_form);
        model.addAttribute("complaint_form", complaint_form);
        return "complaint";
    }
    @RequestMapping(value = { "/profileGuest/{idGuest}" }, method = RequestMethod.GET)
    public String guest (@PathVariable(name = "idGuest") int idGuest, Model model) throws NullFieldException, ErrorFieldException {
        UserEntity user = userService.getById(idGuest).get();
        /*for (QuestionEntity question : questions) {
            question.meLikedBool(user);
        }*/
        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questions = questionService.getReceivedQuestionsAnswer(idGuest);
        if (questions.size() == 0) flag = false;
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questions);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        model.addAttribute("subsc", subscriptionsService.isMySubscription(idUser, idGuest));
        return "userGuestProfile";
    }
    @RequestMapping(value = { "/AnswerQuestionPost" }, method=RequestMethod.POST)
    public String answerQuestionPost(@ModelAttribute("question") QuestionEntity question, Model model) throws IOException, NullFieldException, ErrorFieldException {
        if (question.getAnswer() == NULL) return "redirect:/answer/" + question.getIdQuestion();
        QuestionEntity questionBase = questionService.getById(idQuestion).get();
        questionBase.setAnswer(question.getAnswer());
        questionBase.setStatus("Получен ответ");
        questionService.save(questionBase);
        return "redirect:/profile/newQuestions";
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
    @RequestMapping(value = { "/profile/newQuestions" }, method = RequestMethod.GET)
    public String newQuestions (Model model) throws NullFieldException, ErrorFieldException {
        questions = questionService.getReceivedQuestionsNew(idUser);
        UserEntity user = userService.getById(idUser).get();
        model.addAttribute("Name",user.getName());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("questions", questions);
        return "newQuestions";
    }
    @RequestMapping(value = { "/news" }, method = RequestMethod.GET)
    public String news (Model model) throws NullFieldException, ErrorFieldException {
        questions = questionService.getAll();
        UserEntity user = userService.getById(idUser).get();
        model.addAttribute("Name",user.getName());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("questions", questions);
        return "news";
    }
    @RequestMapping(value = { "/editProfileAdminPost" }, method=RequestMethod.POST)
    public String editProfileAdminPost( Model model) throws IOException, NullFieldException, ErrorFieldException {
        return "redirect:/profileAdmin";
    }
    @RequestMapping(value = { "/editProfilePost" }, method=RequestMethod.POST)
    public String editProfilePost( @ModelAttribute("user") UserEntity user, @RequestParam("photo") MultipartFile multipartFile, Model model) throws IOException, NullFieldException, ErrorFieldException {
         String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
         user = userService.updateUser(user, idUser);
         if (!fileName.isEmpty()) {
             userService.updateAvatar(user, user.makeAvatarImagePath(fileName));
         }
        if (!fileName.isEmpty()) {
            String uploadDir = "user-photos/" + user.getIdUser();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/profile";
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
        model.addAttribute("avatarImagePath", user.getAvatar());
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
        model.addAttribute("avatarImagePath", user.getAvatar());
        return "subscriptions";
    }
    @RequestMapping(value = { "/profileAdmin" }, method = RequestMethod.GET)
    public String adminProfile (Model model) throws NullFieldException, ErrorFieldException {
        AdminEntity admin = adminService.getById(idUser).get();
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
                return "redirect:/";
            }
        }
        return "wrong_log_pas";
    }
    @RequestMapping(value = { "/welcomeFormBack" }, method = RequestMethod.GET)
    public String welcomeFormBack() {
        return "redirect:/";
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

    @RequestMapping(value = { "/complaintSave" }, method = RequestMethod.GET)
    public String complaintSave(@ModelAttribute(value="complaint_form") ComplaintEntity complaint_form) throws NullFieldException, ErrorFieldException {
        QuestionEntity question_form = questionService.getById(idQuestion).get();
        //complaintService.save(new ComplaintEntity(complaint_form.getDescription(), question_form));
        return "redirect:/profile";
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
    @RequestMapping(value = {"/admin_complaint_stat" }, method = RequestMethod.GET)
    public String admin_complaint_stat(Model model) {

        List<ComplaintEntity> complaint = new ArrayList<>();
        for (ComplaintEntity entity : complaintService.getAll()){
            System.out.println(entity);
            //if (entity.getStatus().equals("Принята") || entity.getStatus().equals("Отклонена")){
            if(entity.getAdmin().getIdAdmin().equals(idUser)){
                complaint.add(entity);
            }
        }
        model.addAttribute("complaints", complaint);
        return "admin_complaint_stat";
    }
}
