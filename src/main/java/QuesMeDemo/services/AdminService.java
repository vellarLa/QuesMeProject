package QuesMeDemo.services;

import QuesMeDemo.entities.AdminEntity;
import QuesMeDemo.entities.UserEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
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

    public void updateFullName(Integer id, String fullName) throws ErrorFieldException, NullFieldException {
        AdminEntity adminEntity = getById(id).get();
        adminEntity.setFullName(fullName);
        save(adminEntity);
    }
    public void updatePassword(Integer id, String password) throws ErrorFieldException, NullFieldException {
        AdminEntity adminEntity = getById(id).get();
        adminEntity.setPassword(password);
        save(adminEntity);
    }
    public void rightCount (String property, int countMin, int countMax, String except) throws ErrorFieldException {
        if (property.length() > countMax || property.length()<countMin)
            throw new ErrorFieldException("Неверное количество символов для " + except);
    }
    public void save(AdminEntity adminEntity) throws ErrorFieldException, NullFieldException {
        if (adminEntity.getFullName() == null || adminEntity.getLogin() == null
                || adminEntity.getPassword() == null) {throw new NullFieldException();}
        rightCount(adminEntity.getFullName(), 1,50, "имени.");
        rightCount(adminEntity.getLogin(), 4,15, "логина.");
        rightCount(adminEntity.getPassword(), 5,50, "пароля.");
        List<AdminEntity> all = getAll();
        for (AdminEntity admin : all) {
            if (admin.getLogin().equals(adminEntity.getLogin()) && admin.getIdAdmin() != adminEntity.getIdAdmin()) {
                throw new ErrorFieldException("Логин не уникален.");
            }
        }
        adminRepository.save(adminEntity);
    }
    public List<AdminEntity> getAll() {
        return adminRepository.findAll();
    }

    /*public void saveAll(List<AdminEntity> admins) {
        adminRepository.saveAll(admins);
    }*/
}

