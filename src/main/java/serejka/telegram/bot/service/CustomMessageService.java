package serejka.telegram.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import serejka.telegram.bot.botapi.Bot;
import serejka.telegram.bot.models.User;

import java.util.List;

@Slf4j
@Service
public class CustomMessageService {

    private final UserService userService;
    private final Bot superBot;

    public CustomMessageService(UserService userService, Bot superBot) {
        this.userService = userService;
        this.superBot = superBot;
    }

    public void sendCustomMessageToUser(long chatId, String reply) {
        superBot.sendMessageByAdmin(chatId, reply);
        log.info("Send message to user - {}, with text: {}", chatId, reply);
    }

    public void messageToAllUsers(String reply) {
        ThreadMessage threadMessage = new ThreadMessage();
        threadMessage.setReply(reply);
        threadMessage.start();
    }

    private final class ThreadMessage extends Thread {

        @Override
        public void run() {
            List<User> allUsers = userService.findAllUsers();
            for (User user : allUsers) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                superBot.sendMessageByAdmin(user.getUserId(), reply);
                log.info("Send message to user - {}, with text: {}", user.getUserId(), reply);
            }
        }

        private String reply;

        public void setReply(String reply) {
            this.reply = reply;
        }
    }
}
