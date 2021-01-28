package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import serejka.telegram.bot.cache.UserDataCache;
import serejka.telegram.bot.logic.Bot;
import serejka.telegram.bot.logic.Commands;
import serejka.telegram.bot.models.Movie;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {

    ParserService parserService;
    ReplyToUserService replyService;
    SendMessageService sendMsg;
    UserDataCache userDataCache;
    KeyboardService keyboardService;
    ReplyToUserService replyToUserService;

    public SendMessage searchLogic(Message message) {
        if (message.getText().equals("/cancel")
                || message.getText().equals("Вернуться\uD83D\uDE15")) {
            userDataCache.deleteStateUser(message.getFrom().getId());
            return keyboardService.getKeyboard(message.getChatId(),
                    "Увидимся в следующий раз\uD83D\uDE0A", Commands.START);
        }
        return sendListMoviesSearch(message);
    }


    public SendMessage sendListMovies(Commands command, Message message) {
        List<Movie> movies;

        movies = parserService.getListMovies(command);
        String reply = replyService.replyListMovies(movies, command);
        return sendMsg.sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
    }

    public SendMessage sendListMoviesSearch(Message message) {
        List<Movie> movies;
        movies = parserService.getListMoviesBySearch(message.getText());
        movies.sort(((o1, o2) -> -1 * Float.compare(o1.getVoteAverage(), o2.getVoteAverage())));
        movies.sort(((o1, o2) -> -1 * o1.getVotes() - o2.getVotes()));
        String reply = replyService.replyListMovies(movies, Commands.SEARCH);
        return sendMsg.sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
    }

    private InlineKeyboardMarkup getInlineMessageButtons(List<Movie> list) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            button = new InlineKeyboardButton();
            button.setText((i + 1) + ".");
            button.setCallbackData(String.valueOf(list.get(i).getId()));
            keyboardButtons.add(button);
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }


    @SneakyThrows
    @Async
    public void sendRandomMovie(Message message, Bot superBot) {
        superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
        SecureRandom random = new SecureRandom();
        Movie movie;
        log.info("Thread in async method {}", Thread.currentThread().getName());
        while (true) {
            movie = parserService.parseMovie(random.nextInt(800000));
            if (movie != null && movie.getVotes() > 50 && Integer.parseInt(movie.getYear()) > 1989) {
                String text = replyToUserService.replyMovie(message.getChatId(), String.valueOf(movie.getId()));
                superBot.execute(sendMsg.sendMsg(message.getFrom().getId(), text));
                return;
            }
        }

    }

}
