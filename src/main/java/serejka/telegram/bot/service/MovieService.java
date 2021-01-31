package serejka.telegram.bot.service;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.glassfish.grizzly.threadpool.FixedThreadPool;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {

    static int NUM_OF_FILMS = 800000;
    static int NUM_OF_THREADS = 8;
    static int INITIAL_CASE = NUM_OF_FILMS / NUM_OF_THREADS;

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
        long start = new Date().getTime();
        Movie movie = null;
        int counter = 0;
        SecureRandom secureRandom = new SecureRandom();
        ThreadMovie[] threadMovies = new ThreadMovie[NUM_OF_THREADS];
        for (int i = 0; i < threadMovies.length; i++) {
            threadMovies[i] = initializeThread(counter, secureRandom);
            counter += INITIAL_CASE;
            threadMovies[i].start();
        }
        while (movie == null) {
            for (ThreadMovie threadMovie : threadMovies) {
                if (threadMovie.getMovie() != null) {
                    movie = threadMovie.getMovie();
                    break;
                }
            }
        }
        for (ThreadMovie threadMovie : threadMovies) {
            threadMovie.interrupt();
        }
        String text = replyToUserService.replyMovie(message.getChatId(), String.valueOf(movie.getId()));
        superBot.execute(sendMsg.sendMsg(message.getFrom().getId(), text));
        log.info("Send movie to user with time - {}", new Date().getTime() - start);
    }

    private ThreadMovie initializeThread(int number, SecureRandom random) {
        ThreadMovie threadMovie = new ThreadMovie();
        threadMovie.setNumber(number);
        threadMovie.setRandom(random);
        return threadMovie;
    }

    @Getter
    @Setter
    private final class ThreadMovie extends Thread {

        @Override
        public void run() {
            Movie tempMovie;
            while (!interrupted()) {
                tempMovie = parserService.parseMovie(random.nextInt(INITIAL_CASE) + number);
                if (tempMovie != null && tempMovie.getVotes() > 100 && Integer.parseInt(tempMovie.getYear()) > 1989) {
                    log.info("Find movie {}", tempMovie);
                    movie = tempMovie;
                    return;
                }
            }
        }

        private int number;
        private Movie movie;
        private SecureRandom random;

    }

    private final class ExecutorThread implements Callable<Movie> {

        @Override
        public Movie call() {
            log.info("Work in thread - {}", Thread.currentThread().getName());
            Movie tempMovie;
            SecureRandom secureRandom = new SecureRandom();
            while (true) {
                tempMovie = parserService.parseMovie(secureRandom.nextInt(800000));
                if (tempMovie != null && tempMovie.getVotes() > 100 && Integer.parseInt(tempMovie.getYear()) > 1989) {
                    log.info("Find movie {}", tempMovie);
                    return tempMovie;
                }
            }
        }
    }

}
