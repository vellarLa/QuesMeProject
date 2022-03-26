package QuesMeDemo.services;

import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.repositories.AdminRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public void rightCount (String property, int countMin, int countMax, String except) throws ErrorFieldException {
        if (property.length() > countMax || property.length()<countMin)
            throw new ErrorFieldException("Неверное количество символов для " + except);
    }

    public void save(AdminEntity adminEntity) throws ErrorFieldException {
        rightCount(adminEntity.getFullName(), 1,50, "имени.");
        rightCount(adminEntity.getLogin(), 4,15, "логина.");
        rightCount(adminEntity.getPassword(), 5,50, "пароля.");
        List<AdminEntity> all = getAll();
        for (AdminEntity user : all) {
            if (user.getLogin().equals(adminEntity.getLogin())) {
                throw new ErrorFieldException("Логин не уникален.");
            }
        }
        adminRepository.save(adminEntity);
    }

    public List<AdminEntity> getAll() {
        return adminRepository.findAll();
    }

    public void saveAll(List<AdminEntity> admins) {
        adminRepository.saveAll(admins);
    }
}

