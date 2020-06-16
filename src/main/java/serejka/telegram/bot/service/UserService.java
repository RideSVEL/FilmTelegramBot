package serejka.telegram.bot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.springframework.stereotype.Service;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.UserRepository;

@Service
public class UserService {

    //@Autowired
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
}
