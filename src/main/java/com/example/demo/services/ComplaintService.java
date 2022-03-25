package com.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.entities.ComplaintEntity;
import com.example.demo.repositories.ComplaintRepository;
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

    public void save(ComplaintEntity complaintEntity) {
        complaintRepository.save(complaintEntity);
    }

    public List<ComplaintEntity> getAll() {
        return complaintRepository.findAll();
    }

    public void saveAll(List<ComplaintEntity> questions) {
        complaintRepository.saveAll(questions);
    }
}
