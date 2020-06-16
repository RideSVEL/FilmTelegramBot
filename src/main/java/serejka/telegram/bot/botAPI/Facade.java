package serejka.telegram.bot.botAPI;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.UserService;


@Component
public class Facade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Facade.class);

//
//    @Autowired
//    private UserRepository userRepository;

    private final UserService userService;

    public Facade(UserService userService) {
        this.userService = userService;
    }

    public SendMessage handle(Update update) {
        SendMessage reply = null;
        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (userService.exists(message.getFrom().getId())) {
                log.info(" <||> User already exists!");
            } else {
                User user = new User(message.getChatId(),
                        message.getFrom().getId(), message.getFrom().getUserName(),
                        message.getFrom().getFirstName(), message.getFrom().getLastName());
                log.info(" <||> Save to DB User: {} ", user.toString());
                userService.save(user);
            }
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



            }
            case "/help" -> reply = "Я тебе всегда помогу!";
            case "Привет" -> reply = "И снова мы здороваемся!";
            default -> reply = "Trunk Trunk Trunk";
        }
        return new SendMessage(message.getChatId(), reply);
    }

}
