package serejka.telegram.bot.botapi;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.models.Movie;
import serejka.telegram.bot.service.ParseMovieService;
import serejka.telegram.bot.service.UserService;


@Component
public class Facade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Facade.class);

    private final UserService userService;
    private final ParseMovieService parseMovieService;

    public Facade(UserService userService, ParseMovieService parseMovieService) {
        this.userService = userService;
        this.parseMovieService = parseMovieService;
    }

    public SendMessage handle(Update update) {
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
            default -> {
                try {
                    Movie movie = parseMovieService.parseMovie(Integer.parseInt(message.getText()));
                    reply = "Trunk Trunk Trunk";
                    if (movie != null) {
                        reply = movie.toString();
                    }
                } catch (NumberFormatException e) {
                    reply = "Trunk Trunk Trunk";
                }
            }
        }
        return new SendMessage(message.getChatId(), reply);
    }

}
