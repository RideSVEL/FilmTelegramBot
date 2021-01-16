package serejka.telegram.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.UserRepository;

import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(User user) {
        userRepository.save(user);
    }

    public boolean exists(Integer id) {
        return userRepository.existsUserByUserId(id);
    }

    public List<User> findAllUsers() {
        return (List<User>) userRepository.findAll();
    }

    public void checkAndSave(Message message) {
        if (exists(message.getFrom().getId())) {
            log.info(" <||> User already exists!");
        } else {
            User user = new User(message.getFrom().getId(), message.getFrom().getUserName(),
                    message.getFrom().getFirstName(), message.getFrom().getLastName());
            log.info(" <||> Save to DB User: {} ", user.toString());
            save(user);
        }
    }

    public User findUserByUserId(Integer userId) {
        return userRepository.findUserByUserId(userId);
    }
}
