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

    public void updateById(Integer idUser) {
        Optional<UserEntity> entity = getById(idUser);
    }

    public void rightCount (String property, int countMin, int countMax, String except) throws ErrorFieldException {
        if (property.length() > countMax || property.length()<countMin)
            throw new ErrorFieldException("Неверное количество символов для " + except);
    }
    public void uniqueFields (UserEntity userEntity) throws ErrorFieldException {
        List<UserEntity> all = getAll();
        for (UserEntity user : all) {
            if (user.getLogin().equals(userEntity.getLogin())) {
                throw new ErrorFieldException("Логин не уникален.");
            }
            if (user.getNickname().equals(userEntity.getNickname())) {
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
    }

    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }

    public void saveAll(List<UserEntity> questions) {
        userRepository.saveAll(questions);
    }
}
