package serejka.telegram.bot.logic.bot;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.logic.commands.CommandsRealer;


@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Facade {

    Bot superBot;
    Util util;
    CommandsRealer commandsRealer;

    public Facade(@Lazy Bot superBot, Util util,
                  CommandsRealer commandsRealer) {
        this.superBot = superBot;
        this.util = util;
        this.commandsRealer = commandsRealer;
    }

    public BotApiMethod<?> handle(Update update) {
        return update.hasCallbackQuery() ?
                handleCallback(update.getCallbackQuery()) :
                handleMessage(update.getMessage());
    }

    private SendMessage handleCallback(CallbackQuery callbackQuery) {
        log.info(" <||> New callback from User: {}, with data: {}",
                callbackQuery.getFrom().getUserName(), callbackQuery.getData());
        util.updateCountOfUse(callbackQuery.getFrom().getId());
        return commandsRealer.answerOnUpdate(callbackQuery);
    }

    private SendMessage handleMessage(Message message) {
        if (message != null && message.hasText()) {
            superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
            util.updateStatisticsByCommand(message);
            util.updateCountOfUse(message.getFrom().getId());
            log.info(" <||> New message from User: {}, chatId: {}, with text {}:",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            return commandsRealer.answerOnUpdate(message);
        }
        return null;
    }

}
