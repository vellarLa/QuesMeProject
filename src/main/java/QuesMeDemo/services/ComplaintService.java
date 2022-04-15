package QuesMeDemo.services;

import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.entities.ComplaintEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public void updateAdmin(Integer id, AdminEntity admin) throws ErrorFieldException, NullFieldException {
        ComplaintEntity complaintEntity = getById(id).get();
        complaintEntity.setAdmin(admin);
        save(complaintEntity);
    }
    public void updateStatus(Integer id, String status) throws ErrorFieldException, NullFieldException {
        ComplaintEntity complaintEntity = getById(id).get();
        complaintEntity.setStatus(status);
        save(complaintEntity);
    }

    public void deleteByIdQuestion (Integer idQuestion) {
        List<ComplaintEntity> all = getAll();
        for (ComplaintEntity  complaint: all) {
            if (complaint.getQuestion().getIdQuestion() == idQuestion) {
                delById(complaint.getIdComplaint());
            }
        }
    }

    public void save(ComplaintEntity complaintEntity) throws ErrorFieldException, NullFieldException {
        if (complaintEntity.getDescription() == null) throw new NullFieldException();
        if (complaintEntity.getDescription().length()>999)
            throw new ErrorFieldException("Слишком длинное описание (максимально 1000 символов).");
        List<ComplaintEntity> all = getAll();

        for (ComplaintEntity  complaint: all) {
            if (complaint.getQuestion().getIdQuestion() == complaintEntity.getQuestion().getIdQuestion()) {
                throw new ErrorFieldException("Вы уже подали жалобу на этот вопрос.");
            }
        }
        complaintRepository.save(complaintEntity);

    }
    public List<ComplaintEntity> getAll() {
        return complaintRepository.findAll();
    }

    /*public void saveAll(List<ComplaintEntity> questions) {
        complaintRepository.saveAll(questions);
    }*/
}

