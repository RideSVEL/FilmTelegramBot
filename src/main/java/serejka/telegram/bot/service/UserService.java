package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.UserRepository;
import serejka.telegram.bot.repository.UserRepositoryImpl;

import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;
    UserRepositoryImpl userRepositoryImpl;

    public UserService(UserRepository userRepository, UserRepositoryImpl userRepositoryImpl) {
        this.userRepository = userRepository;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean exists(Integer id) {
        return userRepository.existsUserByUserId(id);
    }

    public List<User> findUsersByGreaterId(Long id, int limit) {
        return userRepositoryImpl.findOrderedByIdLimitingBy(id, limit);
    }

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public void checkAndSave(Message message) {
        if (exists(message.getFrom().getId())) {
            log.info(" <||> User already exists!");
        } else {
            User user = new User(message.getFrom().getId(), message.getFrom().getUserName(),
                    message.getFrom().getFirstName(), message.getFrom().getLastName(), 0);
            log.info(" <||> Save to DB User: {} ", user.toString());
            save(user);
        }
    }

    public User findUserByUserId(Integer userId) {
        return userRepository.findUserByUserId(userId);
    }

    public void updateByUserIdCountOfUse(Integer userId) {
        try {
            User userByUserId = findUserByUserId(userId);
            userByUserId.setCountOfUse(userByUserId.getCountOfUse() + 1);
            save(userByUserId);
        } catch (NullPointerException e) {
            log.error("GET NPE FOR FIRST USER");
        }
    }
}
