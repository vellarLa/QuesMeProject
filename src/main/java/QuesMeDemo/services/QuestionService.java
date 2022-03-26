package QuesMeDemo.services;

import QuesMeDemo.entities.CategoryEntity;
import QuesMeDemo.entities.ComplaintEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import QuesMeDemo.entities.QuestionEntity;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final ComplaintService complaintService;

    public Optional<QuestionEntity> getById(Integer idQuestion) {
        return questionRepository.findById(idQuestion);
    }

    public void delById(Integer idQuestion) {
        complaintService.deleteByIdQuestion(idQuestion);
        questionRepository.deleteById(idQuestion);
    }

    public void updateStatus(Integer id, String status) throws ErrorFieldException, NullFieldException {
        QuestionEntity questionEntity = getById(id).get();
        questionEntity.setStatus(status);
        save(questionEntity);
    }

    public void nullCategory(Integer idCategory) throws ErrorFieldException, NullFieldException {
        List<QuestionEntity> all = getAll();
        for (QuestionEntity  question: all) {
            if (question.getCategory().getIdCategory() == idCategory) {
                question.setCategory(null);
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
    }
    public List<QuestionEntity> getAll() {
        return questionRepository.findAll();
    }
    /*public void saveAll(List<QuestionEntity> questions) {
        questionRepository.saveAll(questions);
    }*/
}
