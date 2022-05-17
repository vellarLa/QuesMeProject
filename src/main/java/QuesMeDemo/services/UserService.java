package QuesMeDemo.services;

import QuesMeDemo.entities.NotificationEntity;
import QuesMeDemo.exeptions.ErrorFieldException;
import QuesMeDemo.exeptions.NullFieldException;
import QuesMeDemo.repositories.UserRepository;
import QuesMeDemo.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    private final NotificationService notificationRepository;
    private final int MAXBAN = 3;

    public Optional<UserEntity> getById(Integer idUser) {
        return userRepository.findById(idUser);
    }

    public void delById(Integer idUser) {
        userRepository.deleteById(idUser);
    }
    public void updateNicknameById(Integer id, String nickname) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setNickname(nickname);
        save(userEntity);
    }
    public void updateAvatarById(Integer id, String avatar) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setAvatar(avatar);
        save(userEntity);
    }
    public void updateNameById(Integer id, String name) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setName(name);
        save(userEntity);
    }
    public void updateSexById(Integer id, Character sex) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setSex(sex);
        save(userEntity);
    }
    public void updateDescriptionById(Integer id, String description) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setDescription(description);
        save(userEntity);
    }
    public void updatePasswordById(Integer id, String password) throws ErrorFieldException, NullFieldException {
        UserEntity userEntity = getById(id).get();
        userEntity.setPassword(password);
        save(userEntity);
    }
    public void updateActiveById(Integer id, Boolean active) throws ErrorFieldException, NullFieldException {
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
        if (ban < MAXBAN)
        {
            NotificationEntity notificationEntity = new NotificationEntity(userEntity, "Бан", ban);
            notificationRepository.save(notificationEntity);
        }

    }
    public void updateNickname(UserEntity user, String nickname) throws ErrorFieldException, NullFieldException {
        user.setNickname(nickname);
        save(user);
    }
    public void updateAvatar(UserEntity user, String avatar) throws ErrorFieldException, NullFieldException {
        user.setAvatar(avatar);
        save(user);
    }
    public void updateName(UserEntity user, String name) throws ErrorFieldException, NullFieldException {
        user.setName(name);
        save(user);
    }
    public void updateSex(UserEntity user, Character sex) throws ErrorFieldException, NullFieldException {
        user.setSex(sex);
        save(user);
    }
    public void updateDescription(UserEntity user, String description) throws ErrorFieldException, NullFieldException {
        user.setDescription(description);
        save(user);
    }
    public void updatePassword(UserEntity user, String password) throws ErrorFieldException, NullFieldException {
        user.setPassword(password);
        save(user);
    }
    public void updateActive(UserEntity user, Boolean active) throws ErrorFieldException, NullFieldException {
        if (active) user.setActive('A');
        else user.setActive('N');
        save(user);
    }

    public void addBan (UserEntity user) throws ErrorFieldException, NullFieldException {
        int ban = user.getBan() + 1;
        user.setBan(ban);
        save(user);
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
        if (userEntity.getBan() == MAXBAN) delById(userEntity.getIdUser());

    }
    public List<UserEntity> getAll() {
        return userRepository.findAll();
    }


    /*public void saveAll(List<UserEntity> questions) {
        userRepository.saveAll(questions);
    }*/
}
