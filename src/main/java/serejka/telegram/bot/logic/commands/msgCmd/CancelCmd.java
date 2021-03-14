package serejka.telegram.bot.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.commands.MessageCommand;
import serejka.telegram.bot.logic.enums.Commands;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CancelCmd implements MessageCommand {

    OtherCmd otherCmd;

    @Override
    public SendMessage generateMessage(Message message) {
        return otherCmd.generateMessage(message);
    }

    @Override
    public Commands getMyCommand() {
        return Commands.CANCEL;
    }
}
