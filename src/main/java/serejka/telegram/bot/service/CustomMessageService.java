package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;
import serejka.telegram.bot.logic.bot.Bot;
import serejka.telegram.bot.models.User;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CustomMessageService {

    UserService userService;
    Bot superBot;

    public void sendCustomMessageToUser(long chatId, String reply) {
        superBot.sendMessageByAdmin(chatId, reply);
        log.info("Send message to user - {}, with text: {}", chatId, reply);
    }

    @SneakyThrows
    @Async
    public void messageToAllUsers(String reply) {
        List<User> allUsers = userService.findAllUsers();
        for (User user : allUsers) {
            Thread.sleep(50);
            try {
                superBot.sendMessageByAdmin(user.getUserId(), reply);
                log.info("Send message to user - {}, with text: {}", user.getUserId(), reply);
            } catch (Exception e) {
                log.error("Chat not found");
            }
        }
    }


}
