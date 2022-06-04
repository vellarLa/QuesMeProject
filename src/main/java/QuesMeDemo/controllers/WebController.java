package QuesMeDemo.controllers;

import QuesMeDemo.entities.*;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NotLoginException;
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
    private static List<QuestionEntity> questionsProfile = new ArrayList<QuestionEntity>();
    private static int idUser;
    private static int idQuestion = 1;
    private static int idGuest = 1;
    private static boolean sort = false;
    private static boolean ques = false;
    private static boolean loginUser = false;
    private static boolean loginAdmin = false;
    private static boolean welcome = false;



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
                loginUser = true;
                return "redirect:/profile";
            }
        }
        List<AdminEntity> allAdmin = adminService.getAll();
        for (AdminEntity entity : allAdmin) {
            if (welcome_form.getLogin().equals(entity.getLogin()) && welcome_form.getPassword().equals(entity.getPassword())){
                idUser = entity.getIdAdmin();
                loginAdmin = true;
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
    public String question(Model model) throws NotLoginException {
        if (!loginUser) throw new NotLoginException();
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
        ques = false;
        return "question";
    }
    @RequestMapping(value = { "/questionTrue" }, method = RequestMethod.GET)
    public String questionGuest(Model model) throws NotLoginException {
        if (!loginUser) throw new NotLoginException();
        QuestionEntity question_form = new QuestionEntity();
        question_form.setReceiver(userService.getById(idGuest).get());
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
    public String processFormSave(@ModelAttribute(value="question_form") QuestionEntity ques) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
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
        userService.save(user);
        if (!fileName.isEmpty()) {
            userService.updateAvatar(user, user.makeAvatarImagePath(fileName));
        }
        else {
            userService.updateAvatar(user, "/img/avatar0.jpg");
        }
        idUser = user.getIdUser();
        loginUser = true;
        if (!fileName.isEmpty()) {
            String uploadDir = "user-photos/" + user.getIdUser();
            FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        }
        return "redirect:/profile";
        //return new RedirectView("/temp", true);
    }
    @RequestMapping(value = { "/profile" }, method = RequestMethod.GET)
    public String profile(Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        UserEntity user = userService.getById(idUser).get();
        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questionsProfile = questionService.getReceivedQuestionsAnswer(idUser);
        if (questionsProfile.size() == 0) flag = false;
        List<UserEntity> allUsers = new ArrayList<>();
        for (UserEntity entity : userService.getAll()){
            if (entity.getIdUser().equals(idUser))
                continue;
            allUsers.add(entity);
        }
        model.addAttribute("allUsers", allUsers);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("NewAnswersNum",  questionService.getReceivedQuestionsNew(idUser).size());
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        return "main_profile";
    }
    @ RequestMapping(value = {"/{nickname}" }, method = RequestMethod.GET)
    public String guestStringNickname (@PathVariable String nickname, Model model) throws NotLoginException {
        if (!loginUser) throw new NotLoginException();
        for (UserEntity entity : userService.getAll()){
            if (entity.getNickname().equals(nickname))
                idGuest = entity.getIdUser();
        }
        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        UserEntity user = userService.getById(idGuest).get();
        questionsProfile = questionService.getReceivedQuestionsAnswer(idGuest);
        if (questionsProfile.size() == 0) flag = false;
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        model.addAttribute("subsc", subscriptionsService.isMySubscription(idUser, user.getIdUser()));
        return "userGuestProfile";
    }
    @RequestMapping(value = { "/updateAvatar" }, method=RequestMethod.POST)
    public String updateAvatarProfile(@ModelAttribute("user") UserEntity user, @RequestParam("photo") MultipartFile multipartFile) throws IOException, NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
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
    public String answerQuestion (@PathVariable(name = "idQuestionAns") int idQuestionAns, Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        QuestionEntity question = questionService.getById(idQuestionAns).get();
        idQuestion = idQuestionAns;
        model.addAttribute("question",question);
        return "answerQuestion";
    }
    @RequestMapping(value = { "/deleteQuestion/{idQuestionAns}" }, method = RequestMethod.GET)
    public String deleteQuestion (@PathVariable(name = "idQuestionAns") int idQuestionDel, Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        questionService.delById(idQuestionDel);
        return "redirect:/profile/newQuestions";
    }
    @RequestMapping(value = { "/profile/sortCategory/{idCategory}" }, method = RequestMethod.GET)
    public String sortCategory (@PathVariable(name = "idCategory") int idCategory, Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        UserEntity user = userService.getById(idUser).get();
        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questionsProfile = categoryService.sort(questionService.getReceivedQuestionsAnswer(idUser), idCategory);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("NewAnswersNum",  questionService.getReceivedQuestionsNew(idUser).size());
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        return "main_profile";
    }
    @RequestMapping(value = { "/profile/sortGuestCategory/{idCategory}" }, method = RequestMethod.GET)
    public String sortCategoryGuest (@PathVariable(name = "idCategory") int idCategory, Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        UserEntity user = userService.getById(idGuest).get();
        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questionsProfile = categoryService.sort(questionService.getReceivedQuestionsAnswer(idGuest), idCategory);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        model.addAttribute("subsc", subscriptionsService.isMySubscription(idUser, idGuest));
        return "userGuestProfile";
    }
    @RequestMapping(value = {"/complaint/{idQuestionCom}" }, method = RequestMethod.GET)
    public String complaint(@PathVariable(name = "idQuestionCom") int idQuestionCom,Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        QuestionEntity question_form = questionService.getById(idQuestionCom).get();
        questionService.updateStatus(idQuestionCom, "Получена жалоба");
        idQuestion=idQuestionCom;
        ComplaintEntity complaint_form = new ComplaintEntity("", question_form);
        model.addAttribute("complaint_form", complaint_form);
        return "complaint";
    }
    @RequestMapping(value = {"profile/DelSubscript/{idDelSubscript}" }, method = RequestMethod.GET)
    public String delFollower (@PathVariable(name = "idDelSubscript") int idDelSubscript,RedirectAttributes redirectAttributes,
                               @RequestHeader(required = false) String referer) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        subscriptionsService.delSubscript(idUser, idDelSubscript);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
    @RequestMapping(value = {"profile/MakeSubscript/{idMakeSubscript}" }, method = RequestMethod.GET)
    public String makeFollower (@PathVariable(name = "idMakeSubscript") int idMakeSubscript, RedirectAttributes redirectAttributes,
                                @RequestHeader(required = false) String referer) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        SubscriptionsEntity subscription = new SubscriptionsEntity(userService.getById(idUser).get(), userService.getById(idMakeSubscript).get());
        subscriptionsService.save(subscription);
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .entrySet()
                .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

        return "redirect:" + components.getPath();
    }
    @RequestMapping(value = { "/profileGuest/{idGuestVisit}" }, method = RequestMethod.GET)
    public String guest (@PathVariable(name = "idGuestVisit") int idGuestVisit, Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        idGuest  = idGuestVisit;
        UserEntity user = userService.getById(idGuest).get();
        /*for (QuestionEntity question : questions) {
            question.meLikedBool(user);
        }*/

        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questionsProfile = questionService.getReceivedQuestionsAnswer(idGuest);
        if (questionsProfile.size() == 0) flag = false;
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        model.addAttribute("subsc", subscriptionsService.isMySubscription(idUser, idGuest));
        return "userGuestProfile";
    }
    @RequestMapping(value = { "/profileGuestString/{nickname}" }, method = RequestMethod.GET)
    public String guestString (@PathVariable(name = "GuestVisit") String GuestVisit, Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        List <UserEntity> all = userService.getAll();
        for (UserEntity user : all) {
            if (user.getNickname().equals(GuestVisit)) {
                idGuest  = user.getIdUser();
            }
        }
        UserEntity user = userService.getById(idGuest).get();
        /*for (QuestionEntity question : questions) {
            question.meLikedBool(user);
        }*/

        boolean flag = true;
        model.addAttribute("categories", categoryService.getAll());
        questionsProfile = questionService.getReceivedQuestionsAnswer(idGuest);
        if (questionsProfile.size() == 0) flag = false;
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("SubscriptionsNum", subscriptionsService.MySubscriptionsNum(user.getIdUser()));
        model.addAttribute("FollowersNum", subscriptionsService.FollowersNum(user.getIdUser()));
        model.addAttribute("AnswersNum", questionService.SentQuestionsNum(user.getIdUser()));
        model.addAttribute("QuestionsNum", questionService.ReceivedQuestionsNum(user.getIdUser()));
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        model.addAttribute("subsc", subscriptionsService.isMySubscription(idUser, idGuest));
        return "userGuestProfile";
    }
    @ RequestMapping(value = { "/AnswerQuestionPost" }, method=RequestMethod.POST)
    public String answerQuestionPost(@ ModelAttribute("question") QuestionEntity question, Model model) throws IOException, NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        if (question.getAnswer() == NULL) return "redirect:/answer/" + question.getIdQuestion();
        QuestionEntity questionBase = questionService.getById(idQuestion).get();
        questionBase.setAnswer(question.getAnswer());
        questionService.updateStatus(questionBase.getIdQuestion(), "Получен ответ");
        return "redirect:/profile/newQuestions";
    }
    @RequestMapping(value = { "/profile/edit" }, method = RequestMethod.GET)
    public String editProfile (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        boolean flag = false;
        UserEntity user = userService.getById(idUser).get();
        if (user.getSex() == 'W') flag = true;
        model.addAttribute("user", user);
        model.addAttribute("flag", flag);
        return "editProfile";
    }
    @RequestMapping(value = { "/profile/newQuestions" }, method = RequestMethod.GET)
    public String newQuestions (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        questionsProfile = questionService.getReceivedQuestionsNew(idUser);
        UserEntity user = userService.getById(idUser).get();
        model.addAttribute("Name",user.getName());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("questions", questionsProfile);
        return "newQuestions";
    }
    @RequestMapping(value = { "/news" }, method = RequestMethod.GET)
    public String news (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        questionsProfile = questionService.getNews(idUser, subscriptionsService.MySubscriptions(idUser));
        UserEntity user = userService.getById(idUser).get();
        model.addAttribute("Name",user.getName());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("questions", questionsProfile);
        model.addAttribute("Head", "Моя лента");
        return "news";
    }
    @RequestMapping(value = { "/myQues" }, method = RequestMethod.GET)
    public String myQues (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        questionsProfile = questionService.getSentQuestions(idUser);
        UserEntity user = userService.getById(idUser).get();
        model.addAttribute("Name",user.getName());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("questions", questionsProfile);
        return "myQuestions";
    }
    @RequestMapping(value = { "/editProfileAdminPost" }, method=RequestMethod.POST)
    public String editProfileAdminPost( @ModelAttribute("admin") AdminEntity admin, Model model) throws IOException, NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginAdmin) throw new NotLoginException();
        admin = adminService.updateAdmin(admin, idUser);
        return "redirect:/profileAdmin";
    }
    @RequestMapping(value = { "/newCategoryPost" }, method=RequestMethod.GET)
    public String newCategoryPost( @ModelAttribute("category") CategoryEntity category,  Model model) throws IOException, NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginAdmin) throw new NotLoginException();
        categoryService.save(category);
        return "redirect:/profileAdmin";
    }
    @RequestMapping(value = { "/editProfilePost" }, method=RequestMethod.POST)
    public String editProfilePost( @ModelAttribute("user") UserEntity user, @RequestParam("photo") MultipartFile multipartFile, Model model) throws IOException, NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
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
    public String editProfileAdmin (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginAdmin) throw new NotLoginException();
        AdminEntity admin = adminService.getById(1).get();
        model.addAttribute("user", admin);
        model.addAttribute("login", admin.getLogin());
        return "editProfileAdmin";
    }

    @RequestMapping(value = { "/profile/followers" }, method = RequestMethod.GET)
    public String myFollowers (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        UserEntity user = userService.getById(idUser).get();
        List<UserEntity> followers = subscriptionsService.makeCondition(subscriptionsService.Followers(idUser), idUser);
        model.addAttribute("followers", followers);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        return "followers";
    }
    @RequestMapping(value = { "/profile/subscriptions" }, method = RequestMethod.GET)
    public String mySubscriptions (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        UserEntity user = userService.getById(idUser).get();
        List<UserEntity> followers = subscriptionsService.MySubscriptions(idUser);
        model.addAttribute("followers", followers);
        model.addAttribute("Name",user.getName());
        model.addAttribute("DescriptionUser", user.getDescription());
        model.addAttribute("Nickname", "@" + user.getNickname());
        model.addAttribute("avatarImagePath", user.getAvatar());
        model.addAttribute("allUsers", userService.getAll());
        return "subscriptions";
    }
    @RequestMapping(value = { "/profileAdmin" }, method = RequestMethod.GET)
    public String adminProfile (Model model) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginAdmin) throw new NotLoginException();
        AdminEntity admin = adminService.getById(idUser).get();
        CategoryEntity category = new CategoryEntity();
        model.addAttribute("FullName", admin.getFullName());
        model.addAttribute("category", category);
        model.addAttribute("categories", categoryService.getAll());
        return "admin_profile";
    }
    @RequestMapping(value = { "/search" }, method = RequestMethod.GET)
    public String search(Model model) throws NotLoginException {
        if (!loginUser) throw new NotLoginException();
        List<UserEntity> allUsers = new ArrayList<>();
        for (UserEntity entity : userService.getAll()){
            if (entity.getIdUser().equals(idUser))
                continue;
            allUsers.add(entity);
            //System.out.println(entity);
        }
        model.addAttribute("allUsers", allUsers);
        return "search";
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
        loginUser=false;
        loginAdmin=false;
        welcome=false;
        return "redirect:/";
        //return "redirect:/complaint";
    }
    @RequestMapping(value = {"/notification" }, method = RequestMethod.GET)
    public String notification(Model model) throws NotLoginException {
        if (!loginUser) throw new NotLoginException();
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
    public String deleteNotification (@PathVariable int idNotification) throws NotLoginException {
        if (!loginUser) throw new NotLoginException();
        notificationService.delById(idNotification);
        return "redirect:/notification";
    }

    @RequestMapping(value = { "/complaintSave" }, method = RequestMethod.GET)
    public String complaintSave(@ModelAttribute(value="complaint_form") ComplaintEntity complaint_form) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginUser) throw new NotLoginException();
        QuestionEntity question_form = questionService.getById(idQuestion).get();
        complaintService.save(new ComplaintEntity(complaint_form.getDescription(), question_form));
        return "redirect:/profile";
    }
    @RequestMapping(value = {"/admin_list_complaint"}, method = RequestMethod.GET)
    public String admin_list_complaint(Model model) throws NotLoginException {
        if (!loginAdmin) throw new NotLoginException();
        List<ComplaintEntity> complaint = new ArrayList<>();
        for (ComplaintEntity entity : complaintService.getAll()){
            if (entity.getStatus().equals("Рассматривается")){
                complaint.add(entity);
            }
        }
        model.addAttribute("complaints", complaint);
        return "admin_list_complaint";
    }
    @ RequestMapping(value="complaints/consider/{idComplaint}", method = RequestMethod.GET)
    public String considerComplaint (@ PathVariable int idComplaint, @ RequestParam(value="action", required=true) String action) throws NullFieldException, ErrorFieldException, NotLoginException {
        if (!loginAdmin) throw new NotLoginException();

        if (action.equals("accept")) {
            complaintService.updateStatus(idComplaint, "Принята");
        }
        if (action.equals("reject")) {
            complaintService.updateStatus(idComplaint, "Отклонена");
        }
        complaintService.updateAdmin(idComplaint,adminService.getById(idUser).get());
        System.out.println(complaintService.getById(idComplaint));
        return "redirect:/admin_list_complaint";
    }
    @ RequestMapping(value = {"/admin_complaint_stat" }, method = RequestMethod.GET)
    public String admin_complaint_stat(Model model) throws NotLoginException {
        if (!loginAdmin) throw new NotLoginException();

        List<ComplaintEntity> complaint = new ArrayList<>();
        for (ComplaintEntity entity : complaintService.getAll()){
            System.out.println(entity);
            if(entity.getAdmin() != null && entity.getAdmin().getIdAdmin().equals(idUser)){
                complaint.add(entity);
            }
        }
        model.addAttribute("complaints", complaint);
        return "admin_complaint_stat";
    }
}
