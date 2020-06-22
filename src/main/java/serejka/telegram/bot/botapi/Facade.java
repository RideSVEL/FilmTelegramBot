package serejka.telegram.bot.botapi;

import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.service.ReplyToUserService;
import serejka.telegram.bot.service.UserService;


@Component
public class Facade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Facade.class);

    private final UserService userService;
    private final ReplyToUserService replyToUserService;

    public Facade(UserService userService, ReplyToUserService replyToUserService) {
        this.userService = userService;
        this.replyToUserService = replyToUserService;
    }

    public BotApiMethod<?> handle(Update update) {
        SendMessage reply = null;
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info(" <||> New message from User: {}, chatId: {}, with text {}:",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            reply = handleInputMessage(message);
        }
        return reply;
    }

    private SendMessage handleInputMessage(Message message) {
        String reply;
        switch (message.getText()) {
            case "/start" -> {
                reply = "Привет, " + message.getFrom().getFirstName() + "!\n Давай пообщаемся! Как у тебя дела?";
                userService.checkAndSave(message);
            }
            case "/help" -> reply = "Я тебе всегда помогу!";
            case "Привет" -> reply = "И снова мы здороваемся!";
            default -> reply = replyToUserService.replyMovie(message);
        }
        return sendMsg(message, reply);
    }

    private SendMessage sendMsg(Message message, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(message.getChatId());
        sendMessage.setText(text).setParseMode("html");
        return sendMessage;
    }

}
