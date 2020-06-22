package serejka.telegram.bot.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.botapi.Bot;

@RestController
public class WebHookController {
    private final Bot superBot;

    public WebHookController(Bot superBot) {
        this.superBot = superBot;
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return superBot.onWebhookUpdateReceived(update);
    }
}
