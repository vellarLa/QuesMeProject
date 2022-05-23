package QuesMeDemo.services;

import QuesMeDemo.entities.*;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ComplaintService complaintService;
    private final NotificationService notificationRepository;
    private boolean duplication = false;

    public Optional<QuestionEntity> getById(Integer idQuestion) {
        return questionRepository.findById(idQuestion);
    }

    public void delById(Integer idQuestion) {
        List<ComplaintEntity> all = complaintService.getAll();
        complaintService.deleteByIdQuestion(idQuestion);
        questionRepository.deleteById(idQuestion);
    }

    public void updateStatus(Integer id, String status) throws ErrorFieldException, NullFieldException {
        QuestionEntity questionEntity = getById(id).get();
        questionEntity.setStatus(status);
        save(questionEntity);
        if (status.equalsIgnoreCase("Получен ответ"))
        {
            NotificationEntity notificationEntity = new NotificationEntity("Ответ", questionEntity);
            notificationRepository.save(notificationEntity);
        }
        if (status.equalsIgnoreCase("Получена жалоба"))
        {
            NotificationEntity notificationEntity = new NotificationEntity("Жалоба на вопрос", questionEntity);
            notificationRepository.save(notificationEntity);
        }
    }

    public void nullCategory(Integer idCategory) throws ErrorFieldException, NullFieldException {
        List<QuestionEntity> all = getAll();
        for (QuestionEntity  question: all) {
            if (question.getCategory().getIdCategory() == idCategory) {
                question.setCategory(null);
                duplication = true;
                save(question);

            }
        }
    }

    public void save(QuestionEntity questionEntity) throws NullFieldException, ErrorFieldException {

        if (questionEntity.getSender() == null || questionEntity.getReceiver() == null || questionEntity.getText() == null)
        throw new NullFieldException();
        if (questionEntity.getSender().getIdUser() == questionEntity.getReceiver().getIdUser())
            throw new ErrorFieldException("Отправитель и получатель совпадают!");
        if (questionEntity.getText().length()>999)
            throw new ErrorFieldException("Слишком длинный вопрос (максимально 1000 символов).");

        questionRepository.save(questionEntity);

        if (!duplication)
        {
            NotificationEntity notificationEntity = new NotificationEntity("Новый вопрос", questionEntity);
            for (NotificationEntity entity : notificationRepository.getAll()){
                if(entity.getContent().equals(notificationEntity.getContent())&&entity.getOwner().equals(notificationEntity.getOwner()))
                    duplication = true;
            }
            if(!duplication)
                notificationRepository.save(notificationEntity);
        }
        else
            duplication = false;
    }

    public List<QuestionEntity> getReceivedQuestionsNew(int id) {
        List<QuestionEntity> all = questionRepository.findAll();
        List<QuestionEntity> rez = new ArrayList<QuestionEntity>();
        for (QuestionEntity  question: all) {
            if (question.getReceiver().getIdUser() == id && Objects.equals(question.getStatus(), "Отправлен")) {
                rez.add(question);
            }
        }
        return rez;
    }

    public List<QuestionEntity> getSentQuestions(int id) {
        List<QuestionEntity> all = questionRepository.findAll();
        List<QuestionEntity> rez = new ArrayList<QuestionEntity>();
        for (QuestionEntity  question: all) {
            if (question.getSender().getIdUser() == id) {
                rez.add(question);
            }
        }
        return rez;
    }
    public List<QuestionEntity> getReceivedQuestionsAnswer(int id) {
        List<QuestionEntity> all = questionRepository.findAll();
        List<QuestionEntity> rez = new ArrayList<QuestionEntity>();
        for (QuestionEntity  question: all) {
            if (question.getReceiver().getIdUser() == id && Objects.equals(question.getStatus(), "Получен ответ")) {
                rez.add(question);
            }
        }
        return rez;
    }
    public int ReceivedQuestionsNum(int id) {
        List<QuestionEntity> all = questionRepository.findAll();
        int num = 0;
        for (QuestionEntity  question: all) {
            if (question.getReceiver().getIdUser() == id) {
                num++;
            }
        }
        return num;
    }

    public int SentQuestionsNum(int id) {
        List<QuestionEntity> all = questionRepository.findAll();
        int num = 0;
        for (QuestionEntity  question: all) {
            if (question.getSender().getIdUser() == id) {
                num++;
            }
        }
        return num;
    }


    public List<QuestionEntity> getAll() {
        return questionRepository.findAll();
    }
    /*public void saveAll(List<QuestionEntity> questions) {
        questionRepository.saveAll(questions);
    }*/
    public List<QuestionEntity> checkAnonim (List<QuestionEntity> questions){
        for (QuestionEntity  question: questions) {
            if (question.getAnonymous() == 'Y') {
            }
        }
        return questions;
    }
    public List<QuestionEntity> getNews (Integer idUser, List<UserEntity> subscript) {
        List<QuestionEntity> all = questionRepository.findAll();;
        List<QuestionEntity> rez = new ArrayList<QuestionEntity>();
        for (QuestionEntity  question: all) {
            if (Objects.equals(question.getStatus(), "Получен ответ")) {
                for (UserEntity  user: subscript) {
                    if (user.getIdUser()==question.getReceiver().getIdUser()) {
                        rez.add(question);
                    }
                }
            }
        }
        return rez;
    }

}
