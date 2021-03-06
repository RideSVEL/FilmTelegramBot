package serejka.telegram.bot.logic.commands.stateCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.commands.StateCommand;
import serejka.telegram.bot.logic.enums.BotState;
import serejka.telegram.bot.service.ReviewService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewState implements StateCommand {

    ReviewService reviewService;

    @Override
    public SendMessage generateMessage(Message message) {
        return reviewService.reviewLogic(message);
    }

    @Override
    public BotState getMyCommand() {
        return BotState.REVIEW;
    }
}
