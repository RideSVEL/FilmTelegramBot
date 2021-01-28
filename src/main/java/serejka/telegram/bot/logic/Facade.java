package serejka.telegram.bot.logic;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import serejka.telegram.bot.cache.UserDataCache;


@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class Facade {

    Bot superBot;
    UserDataCache userDataCache;
    Logic logic;

    public Facade(@Lazy Bot superBot,
                  UserDataCache userDataCache, Logic logic) {
        this.superBot = superBot;
        this.userDataCache = userDataCache;
        this.logic = logic;
    }

    public BotApiMethod<?> handle(Update update) {
        SendMessage reply = null;
        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info(" <||> New callback from User: {}, with data: {}",
                    callbackQuery.getFrom().getUserName(), callbackQuery.getData());
            return logic.replyMovieCallback(callbackQuery);
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            superBot.sendChatActionUpdate(update.getMessage().getChatId(), ActionType.TYPING);
            logic.updateStatisticsByCommand(message);
            log.info(" <||> New message from User: {}, chatId: {}, with text {}:",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());
            if (userDataCache.checkContainsKey(message.getFrom().getId())) {
                reply = handleBotStateMessage(message);
            } else {
                reply = handleInputMessage(message);
            }
        }
        return reply;
    }

    private SendMessage handleInputMessage(Message message) {
        String reply;
        Commands commands = Commands.getName(message.getText());
        switch (commands) {
            case START:
                return logic.startCommand(message);
            case HELP:
                reply = logic.replyHelp();
                break;
            case TOPWEEK:
                return logic.listMovieCommand(message, Commands.TOPWEEK);
            case TOPDAY:
                return logic.listMovieCommand(message, Commands.TOPDAY);
            case TOP:
                return logic.listMovieCommand(message, Commands.TOP);
            case REVIEW:
                userDataCache.setUserState(message.getFrom().getId(), BotState.REVIEW);
                return logic.replyReviewKeyboard(message);
            case SEARCH:
                userDataCache.setUserState(message.getFrom().getId(), BotState.SEARCH);
                return logic.replySearchKeyboard(message);
            case RANDOM:
                reply = logic.sendRandomMovie(message, superBot);
                break;
            default:
                reply = logic.replyDefaultMovieById(message);
        }
        return logic.replyMainKeyboard(message, reply);
    }


    private SendMessage handleBotStateMessage(Message message) {
        String reply = null;
        BotState botState = userDataCache.getUserBotState(message.getFrom().getId());
        if (botState != null) {
            switch (botState) {
                case REVIEW -> {
                    return logic.botStateReview(message);
                }
                case SEARCH -> {
                    return logic.botStateSearch(message);
                }
            }
        } else {
            reply = logic.replyError();
        }
        return logic.replyMainKeyboard(message, reply);
    }

}
