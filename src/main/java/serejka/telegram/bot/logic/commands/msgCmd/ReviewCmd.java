package serejka.telegram.bot.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.cache.UserDataCache;
import serejka.telegram.bot.logic.enums.BotState;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.logic.commands.MessageCommand;
import serejka.telegram.bot.service.KeyboardService;
import serejka.telegram.bot.service.ReplyToUserService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewCmd implements MessageCommand {
    UserDataCache userDataCache;
    KeyboardService keyboardService;
    ReplyToUserService replyToUserService;

    @Override
    public SendMessage generateMessage(Message message) {
        userDataCache.setUserState(message.getFrom().getId(), BotState.REVIEW);
        return keyboardService.getKeyboard(message.getChatId(),
                replyToUserService.replyReview(), Commands.REVIEW);
    }

    @Override
    public Commands getMyCommand() {
        return Commands.REVIEW;
    }
}
