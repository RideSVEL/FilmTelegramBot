package serejka.telegram.bot.service;

import lombok.*;
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
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class MovieService {

    static int NUM_OF_FILMS = 800000;
    static int NUM_OF_THREADS = 8;
    static int INITIAL_CASE = NUM_OF_FILMS / NUM_OF_THREADS;
//    static boolean flag = true;

    ParserService parserService;
    ReplyToUserService replyService;
    SendMessageService sendMsg;
    UserDataCache userDataCache;
    KeyboardService keyboardService;
    ReplyToUserService replyToUserService;

    ThreadMovie[] threadMovies = new ThreadMovie[NUM_OF_THREADS];

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
        SecureRandom secureRandom = new SecureRandom();
        updateTaskForThreadsAndStart(secureRandom);
        Movie movie = getMovieFromAnyThread();
        stopAllThreads();
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

    private void updateTaskForThreadsAndStart(SecureRandom random) {
        int counter = 0;
        for (int i = 0; i < threadMovies.length; i++) {
            threadMovies[i] = initializeThread(counter, random);
            counter += INITIAL_CASE;
            threadMovies[i].start();
        }
    }

    private Movie getMovieFromAnyThread() {
        Movie movie;
        while (true) {
            for (ThreadMovie threadMovie : threadMovies) {
                if (threadMovie.getMovie() != null) {
                    movie = threadMovie.getMovie();
                    return movie;
                }
            }
        }
    }

    private void stopAllThreads() {
        for (ThreadMovie threadMovie : threadMovies) {
            threadMovie.interrupt();
        }
    }


//    private ExecutorThread initializeCallable(int number, SecureRandom random) {
//        ExecutorThread executorThread = new ExecutorThread();
//        executorThread.setNumber(number);
//        executorThread.setRandom(random);
//        return executorThread;
//    }
//
//    private List<Callable<Movie>> createListOfTasks(SecureRandom random) {
//        List<Callable<Movie>> moviesTasks = new ArrayList<>();
//        int counter = 0;
//        for (int i = 0; i < NUM_OF_THREADS; i++) {
//            moviesTasks.add(initializeCallable(counter, random));
//            counter += INITIAL_CASE;
//        }
//        return moviesTasks;
//    }

    @Getter
    @Setter
    private final class ThreadMovie extends Thread {

        @Override
        public void run() {
            Movie tempMovie;
            log.info("Work in thread - {}", Thread.currentThread().getName());
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

//    @Getter
//    @Setter
//    private final class ExecutorThread implements Callable<Movie> {
//
//        private SecureRandom random;
//        private int number;
//
//        @Override
//        public Movie call() {
//            log.info("Work in thread - {}", Thread.currentThread().getName());
//            Movie movie;
//            while (true) {
//                movie = parserService.parseMovie(random.nextInt(INITIAL_CASE) + number);
//                if (movie != null && movie.getVotes() > 100 && Integer.parseInt(movie.getYear()) > 1989) {
//                    log.info("Find movie {}", movie);
//                    flag = false;
//                    return movie;
//                }
//            }
//        }
//
//    }

}
