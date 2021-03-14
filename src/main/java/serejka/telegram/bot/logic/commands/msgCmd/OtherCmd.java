package serejka.telegram.bot.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.logic.commands.MessageCommand;
import serejka.telegram.bot.service.KeyboardService;
import serejka.telegram.bot.service.ReplyToUserService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OtherCmd implements MessageCommand {
    KeyboardService keyboardService;
    ReplyToUserService replyToUserService;

    @Override
    public SendMessage generateMessage(Message message) {
        return keyboardService.getKeyboard(message.getChatId(),
                replyToUserService.replyError(), Commands.OTHER);
    }

    @Override
    public Commands getMyCommand() {
        return Commands.OTHER;
    }
}
