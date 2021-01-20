package serejka.telegram.bot.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.botapi.Bot;

@RequiredArgsConstructor
@RestController
public class WebHookController {
    private final Bot superBot;

    @PostMapping("/")
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update) {
        return superBot.onWebhookUpdateReceived(update);
    }


}
