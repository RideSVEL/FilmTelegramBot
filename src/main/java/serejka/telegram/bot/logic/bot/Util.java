package serejka.telegram.bot.logic.bot;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.service.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class Util {

    ReplyToUserService replyToUserService;
    StatisticsService statisticsService;
    ReviewService reviewService;
    KeyboardService keyboardService;
    MovieService movieService;
    UserService userService;

    public SendMessage botStateReview(Message message) {
        return reviewService.reviewLogic(message);
    }

    public SendMessage botStateSearch(Message message) {
        return movieService.searchLogic(message);
    }

    public String replyError() {
        return replyToUserService.replyError();
    }

    public SendMessage replyMainKeyboard(Message message, String reply) {
        return keyboardService.getKeyboard(message.getChatId(),
                reply, Commands.OTHER);
    }

    public void updateStatisticsByCommand(Message message) {
        statisticsService.updateStatisticCommand(message);
    }

    public void updateCountOfUse(Integer userId) {
        userService.updateByUserIdCountOfUse(userId);
    }

}
