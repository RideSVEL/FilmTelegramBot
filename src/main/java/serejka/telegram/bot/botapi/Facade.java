package serejka.telegram.bot.botapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import serejka.telegram.bot.cache.UserDataCache;
import serejka.telegram.bot.models.Movie;
import serejka.telegram.bot.models.Review;
import serejka.telegram.bot.models.User;
import serejka.telegram.bot.service.*;

import java.io.IOException;
import java.util.*;


@Slf4j
@Component
public class Facade {

    private final ReplyToUserService replyToUserService;
    private final ParserService parserService;
    private final StatisticsService statisticsService;
    private final Bot superBot;
    private final UserDataCache userDataCache;
    private final ReviewService reviewService;
    private final UserService userService;
    private final KeyboardService keyboardService;


    public Facade(ReplyToUserService replyToUserService, ParserService parserService,
                  StatisticsService statisticsService, @Lazy Bot superBot,
                  UserDataCache userDataCache, ReviewService reviewService, UserService userService, KeyboardService keyboardService) {
        this.replyToUserService = replyToUserService;
        this.parserService = parserService;
        this.statisticsService = statisticsService;
        this.superBot = superBot;
        this.userDataCache = userDataCache;
        this.reviewService = reviewService;
        this.userService = userService;
        this.keyboardService = keyboardService;
    }

    public BotApiMethod<?> handle(Update update) throws IOException {
        SendMessage reply = null;

        if (update.hasCallbackQuery()) {
            CallbackQuery callbackQuery = update.getCallbackQuery();
            log.info(" <||> New callback from User: {}, with data: {}",
                    callbackQuery.getFrom().getUserName(), callbackQuery.getData());
            return sendMsg(callbackQuery.getFrom().getId(),
                    replyToUserService.replyMovie(callbackQuery.getFrom().getId(), callbackQuery.getData()));
        }

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
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

    private SendMessage handleInputMessage(Message message) throws IOException {
        String reply;
        updateStatisticCommand(message);
        switch (message.getText()) {
            case "/start":
                return keyboardService.getKeyboard(message.getChatId(),
                        replyToUserService.replyStart(message), Commands.START);
            case "Помощь\uD83C\uDD98":
            case "/help":
                reply = "Я тебе всегда помогу!";
                break;
            case "Привет":
                reply = "И снова мы здороваемся!";
                break;
            case "TOP Недели\uD83D\uDE0E":
            case "/topweek":
                return sendListMovies(Commands.TOPWEEK, message);
            case "Новинки\uD83C\uDD95":
            case "/topday":
                return sendListMovies(Commands.TOPDAY, message);
            case "TOP\uD83D\uDD25":
            case "/top":
                return sendListMovies(Commands.TOP, message);
            case "Оставить отзыв\uD83D\uDE4B\u200D♂️":
            case "/review":
                superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
                userDataCache.setUserState(message.getFrom().getId(), BotState.REVIEW);
                return keyboardService.getKeyboard(message.getChatId(),
                        replyToUserService.replyReview(), Commands.REVIEW);
            case "Поиск\uD83D\uDD0D":
            case "/search":
                superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
                userDataCache.setUserState(message.getFrom().getId(), BotState.SEARCH);
                return keyboardService.getKeyboard(message.getChatId(),
                        replyToUserService.replySearch(), Commands.SEARCH);
            default:
                reply = replyToUserService.replyMovie(message.getChatId(), message.getText());
                break;
        }
        return keyboardService.getKeyboard(message.getChatId(),
                reply, Commands.OTHER);
    }

    private void updateStatisticCommand(Message message) {
        Commands name = Commands.getName(message.getText());
        statisticsService.updateCountCommand(name);
    }

    private SendMessage sendListMovies(Commands command, Message message) throws IOException {
        List<Movie> movies;
        superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
        movies = parserService.getListMovies(command);
        String reply = replyToUserService.replyListMovies(movies, command);
        return sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
    }

    private SendMessage sendListMoviesSearch(Message message) throws IOException {
        List<Movie> movies;
        superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
        movies = parserService.getListMoviesBySearch(message.getText());
        movies.sort(((o1, o2) -> -1 * Float.compare(o1.getVoteAverage(), o2.getVoteAverage())));
        movies.sort(((o1, o2) -> -1 * o1.getVotes() - o2.getVotes()));
        String reply = replyToUserService.replyListMovies(movies, Commands.SEARCH);
        return sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
    }

    private SendMessage handleBotStateMessage(Message message) throws IOException {
        String reply = null;
        BotState botState = userDataCache.getUserBotState(message.getFrom().getId());
        if (botState != null) {
            switch (botState) {
                case REVIEW -> {
                    return reviewLogic(message);
                }
                case SEARCH -> {
                    return searchLogic(message);
                }
            }
        } else {
            reply = replyToUserService.replyError();
        }
        return keyboardService.getKeyboard(message.getChatId(),
                reply, Commands.OTHER);
    }

    private SendMessage searchLogic(Message message) throws IOException {
        if (message.getText().equals("/cancel")
                || message.getText().equals("Вернуться\uD83D\uDE15")) {
            userDataCache.deleteStateUser(message.getFrom().getId());
            return keyboardService.getKeyboard(message.getChatId(),
                    "Увидимся в следующий раз\uD83D\uDE0A", Commands.START);
        }
        return sendListMoviesSearch(message);
    }

    private SendMessage reviewLogic(Message message) {
        if (message.getText().equals("/cancel")
                || message.getText().equals("Вернуться\uD83D\uDE15")) {
            userDataCache.deleteStateUser(message.getFrom().getId());
            return keyboardService.getKeyboard(message.getChatId(),
                    "Увидимся в следующий раз\uD83D\uDE0A", Commands.START);
        }
        User user = userService.findUserByUserId(message.getFrom().getId());
        String reply;
        if (user != null) {
            Review review = new Review();
            review.setReview(message.getText());
            review.setUserId(message.getFrom().getId());
            review.setDate(new Date().toString());
            try {
                reviewService.save(review);
                log.info(" <||> Save to DB new review from user: {}", message.getFrom().getId());
                reply = "Братан, спасибо тебе большое за отзыв! Ты лучший!";
                userDataCache.deleteStateUser(message.getFrom().getId());
            } catch (Exception e) {
                reply = "Шось не то, прости пожалуйста((";
                userDataCache.deleteStateUser(message.getFrom().getId());
            }
        } else {
            reply = replyToUserService.replyError();
            userDataCache.deleteStateUser(message.getFrom().getId());
        }
        return keyboardService.getKeyboard(message.getChatId(),
                reply, Commands.OTHER);
    }

    private SendMessage sendMsg(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text).setParseMode("html");
        return sendMessage;
    }

    private SendMessage sendMsg(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text).setParseMode("html");
        return sendMessage;
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

}
