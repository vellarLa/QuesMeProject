package QuesMeDemo.services;

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

    public Optional<QuestionEntity> getById(Integer idQuestion) {
        return questionRepository.findById(idQuestion);
    }

    public void delById(Integer idQuestion) {
        questionRepository.deleteById(idQuestion);
    }

    public void save(QuestionEntity questionEntity) {
        questionRepository.save(questionEntity);
    }

    public List<QuestionEntity> getAll() {
        return questionRepository.findAll();
    }

    public void saveAll(List<QuestionEntity> questions) {
        questionRepository.saveAll(questions);
    }
}
