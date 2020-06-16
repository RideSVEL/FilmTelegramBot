package serejka.telegram.bot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.botAPI.Bot;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.UserRepository;

@RestController
public class WebHookController {
    private final Bot superBot;

//    @Autowired
//    private UserRepository userRepository;

    public WebHookController(Bot superBot) {
        this.superBot = superBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
//                        if (userRepository.findUserByUserId(update.getMessage().getFrom().getId()) != null) {
//                            System.out.println("существует");
//                } else {
//                User user = new User(update.getMessage().getChatId(),
//                        update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName(),
//                        update.getMessage().getFrom().getFirstName(), update.getMessage().getFrom().getLastName());
//              //  log.info("Save to DB User: {} ", user.toString());
//                userRepository.save(user);
//                            System.out.println("сохраненно");
//              }
        return superBot.onWebhookUpdateReceived(update);
    }
}
