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
import serejka.telegram.bot.cache.UserDataCache;
import serejka.telegram.bot.logic.commands.CommandsRealer;
import serejka.telegram.bot.logic.enums.BotState;


@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Facade {

    Bot superBot;
    UserDataCache userDataCache;
    Util util;
    CommandsRealer commandsRealer;


    public Facade(@Lazy Bot superBot,
                  UserDataCache userDataCache, Util util, CommandsRealer commandsRealer) {
        this.superBot = superBot;
        this.userDataCache = userDataCache;
        this.util = util;
        this.commandsRealer = commandsRealer;
    }

    public BotApiMethod<?> handle(Update update) {
        SendMessage reply = null;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info(" <||> New callback from User: {}, with data: {}",
                    callbackQuery.getFrom().getUserName(), callbackQuery.getData());
            util.updateCountOfUse(callbackQuery.getFrom().getId());
            return commandsRealer.answerOnUpdate(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            superBot.sendChatActionUpdate(update.getMessage().getChatId(), ActionType.TYPING);
            util.updateStatisticsByCommand(message);
            util.updateCountOfUse(message.getFrom().getId());
            log.info(" <||> New message from User: {}, chatId: {}, with text {}:",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            if (userDataCache.checkContainsKey(message.getFrom().getId())) {
                reply = handleBotStateMessage(message);
            } else {
                reply = commandsRealer.answerOnUpdate(message);
            }
        }
        return reply;
    }


    private SendMessage handleBotStateMessage(Message message) {
        String reply = null;
        BotState botState = userDataCache.getUserBotState(message.getFrom().getId());
        if (botState != null) {
            switch (botState) {
                case REVIEW -> {
                    return util.botStateReview(message);
                }
                case SEARCH -> {
                    return util.botStateSearch(message);
                }
            }
        } else {
            reply = util.replyError();
        }
        return util.replyMainKeyboard(message, reply);
    }

}
