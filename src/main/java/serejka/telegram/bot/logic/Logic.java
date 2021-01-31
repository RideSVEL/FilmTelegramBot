package serejka.telegram.bot.logic;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.service.*;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class Logic {

    ReplyToUserService replyToUserService;
    StatisticsService statisticsService;
    ReviewService reviewService;
    KeyboardService keyboardService;
    MovieService movieService;
    SendMessageService sendMsg;
    UserService userService;

    public SendMessage startCommand(Message message) {
        return keyboardService.getKeyboard(message.getChatId(),
                replyToUserService.replyStart(message), Commands.START);
    }

    public SendMessage listMovieCommand(Message message, Commands command) {
        return movieService.sendListMovies(command, message);
    }

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

    public SendMessage replyReviewKeyboard(Message message) {
        return keyboardService.getKeyboard(message.getChatId(),
                replyToUserService.replyReview(), Commands.REVIEW);
    }

    public SendMessage replySearchKeyboard(Message message) {
        return keyboardService.getKeyboard(message.getChatId(),
                replyToUserService.replySearch(), Commands.SEARCH);
    }

    public String replyDefaultMovieById(Message message) {
        return replyToUserService.replyMovie(message.getChatId(), message.getText());
    }

    public void updateStatisticsByCommand(Message message) {
        statisticsService.updateStatisticCommand(message);
    }

    public SendMessage replyMovieCallback(CallbackQuery callbackQuery) {
        return sendMsg.sendMsg(callbackQuery.getFrom().getId(),
                replyToUserService.replyMovie(callbackQuery.getFrom().getId(), callbackQuery.getData()));
    }

    public String replyHelp() {
        return replyToUserService.replyHelp();
    }

    @SneakyThrows
    public String sendRandomMovie(Message message, Bot superBot) {
//        superBot.execute(sendMsg.sendMsg(message.getChatId(),
//                replyToUserService.replyCheckMessageToUser(message)));
        superBot.sendDiceForWaiting(message.getChatId());
        movieService.sendRandomMovie(message, superBot);
        return replyToUserService.senWaiting(message);

    }

    public void updateCountOfUse(Integer userId) {
        userService.updateByUserIdCountOfUse(userId);
    }

}
