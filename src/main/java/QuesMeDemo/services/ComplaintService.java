package QuesMeDemo.services;

import QuesMeDemo.entities.ComplaintEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public Optional<ComplaintEntity> getById(Integer idComplaint) {return complaintRepository.findById(idComplaint);}

    public void delById(Integer idComplaint) {
        complaintRepository.deleteById(idComplaint);
    }

    public void save(ComplaintEntity complaintEntity) throws ErrorFieldException, NullFieldException {
        if (complaintEntity.getDescription() == null) throw new NullFieldException();
        if (complaintEntity.getDescription().length()>999)
            throw new ErrorFieldException("Слишком длинное описание (максимально 1000 символов).");
        List<ComplaintEntity> all = getAll();
        for (ComplaintEntity  complaint: all) {
            if (complaint.getQuestion().getIdQuestion().equals(complaintEntity.getQuestion().getIdQuestion())) {
                throw new ErrorFieldException("Вы уже подали жалобу на этот вопрос.");
            }
        }
        complaintRepository.save(complaintEntity);
    }

    public List<ComplaintEntity> getAll() {
        return complaintRepository.findAll();
    }

    public void saveAll(List<ComplaintEntity> questions) {
        complaintRepository.saveAll(questions);
    }
}

