package com.example.demo.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.example.demo.entities.AdminEntity;
import com.example.demo.repositories.AdminRepository;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;

    public Optional<AdminEntity> getById(Integer idAdmin) {
        return adminRepository.findById(idAdmin);
    }

    public void delById(Integer idAdmin) {
        adminRepository.deleteById(idAdmin);
    }

    public void save(AdminEntity adminEntity) {
        adminRepository.save(adminEntity);
    }

    public List<AdminEntity> getAll() {
        return adminRepository.findAll();
    }

    public void saveAll(List<AdminEntity> admins) {
        adminRepository.saveAll(admins);
    }
}

