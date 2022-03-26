package QuesMeDemo.services;

import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.UserRepository;
import QuesMeDemo.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserEntity> getById(Integer idUser) {
        return userRepository.findById(idUser);
    }
    public void delById(Integer idUser) {
        userRepository.deleteById(idUser);
    }
    public void updateNickname(Integer id, String nickname) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setNickname(nickname);
        save(userEntity);
    }
    public void updateName(Integer id, String name) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setName(name);
        save(userEntity);
    }
    public void updateSex(Integer id, Character sex) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setSex(sex);
        save(userEntity);
    }
    public void updateDescription(Integer id, String description) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setDescription(description);
        save(userEntity);
    }
    public void updatePassword(Integer id, String password) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setPassword(password);
        save(userEntity);
    }
    public void updateActive(Integer id, Boolean active) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        if (active) userEntity.setActive('A');
        else userEntity.setActive('N');
        save(userEntity);
    }
    public void addBan (Integer id) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        int ban = userEntity.getBan() + 1;
        userEntity.setBan(ban);
        save(userEntity);
    }
    public void rightCount (String property, int countMin, int countMax, String except) throws ErrorFieldException {
        if (property.length() > countMax || property.length()<countMin)
            throw new ErrorFieldException("Неверное количество символов для " + except);
    }
    public void uniqueFields (UserEntity userEntity) throws ErrorFieldException {
        List<UserEntity> all = getAll();
        for (UserEntity user : all) {
            if (user.getLogin().equals(userEntity.getLogin()) && user.getIdUser() != userEntity.getIdUser()) {
                throw new ErrorFieldException("Логин не уникален.");
            }
            if (user.getNickname().equals(userEntity.getNickname()) && user.getIdUser() != userEntity.getIdUser()) {
                throw new ErrorFieldException("Никнейм не уникален.");
            }
        }
    }
    public void save(UserEntity userEntity) throws NullFieldException, ErrorFieldException {
        if (userEntity.getNickname() == null || userEntity.getName() == null || userEntity.getLogin() == null
        || userEntity.getPassword() == null) {throw new NullFieldException();}
        rightCount(userEntity.getName(), 1,50, "имени.");
        rightCount(userEntity.getNickname(), 4,15, "никнейма.");
        rightCount(userEntity.getLogin(), 4,15, "логина.");
        rightCount(userEntity.getPassword(), 5,15, "пароля.");
        uniqueFields(userEntity);
        userRepository.save(userEntity);
        if (userEntity.getBan() == 3) delById(userEntity.getIdUser());

    }
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    /*public void saveAll(List<UserEntity> questions) {
        userRepository.saveAll(questions);
    }*/
}
