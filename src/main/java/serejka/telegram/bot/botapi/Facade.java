package serejka.telegram.bot.botapi;

import org.slf4j.Logger;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Component
public class Facade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Facade.class);

    private final ReplyToUserService replyToUserService;
    private final ParserService parserService;
    private final StatisticsService statisticsService;
    private final Bot superBot;
    private final UserDataCache userDataCache;
    private final ReviewService reviewService;
    private final UserService userService;

    public Facade(ReplyToUserService replyToUserService, ParserService parserService,
                  StatisticsService statisticsService, @Lazy Bot superBot,
                  UserDataCache userDataCache, ReviewService reviewService, UserService userService) {
        this.replyToUserService = replyToUserService;
        this.parserService = parserService;
        this.statisticsService = statisticsService;
        this.superBot = superBot;
        this.userDataCache = userDataCache;
        this.reviewService = reviewService;
        this.userService = userService;
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
        List<Movie> movies;
        Commands name = Commands.getName(message.getText());
        statisticsService.updateCountCommand(name);
        switch (message.getText()) {
            case "/start" -> reply = replyToUserService.replyStart(message);
            case "/help" -> reply = "Я тебе всегда помогу!";
            case "Привет" -> reply = "И снова мы здороваемся!";
            case "/topweek" -> {
                superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
                movies = parserService.getListMovies(Commands.TOPWEEK);
                reply = replyToUserService.replyListMovies(movies, Commands.TOPWEEK);
                return sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
            }
            case "/topday" -> {
                superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
                movies = parserService.getListMovies(Commands.TOPDAY);
                reply = replyToUserService.replyListMovies(movies, Commands.TOPDAY);
                return sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
            }
            case "/top" -> {
                superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
                movies = parserService.getListMovies(Commands.TOP);
                reply = replyToUserService.replyListMovies(movies, Commands.TOP);
                return sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
            }
            case "/review" -> {
                superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
                reply = "Я рад, что ты решил оставить отзыв о нашем боте," +
                        " отправь свои пожелания\uD83D\uDE0C" +
                        "\nЛибо можешь отменить операцию командой - /cancel\uD83D\uDE15";
                userDataCache.setUserState(message.getFrom().getId(), BotState.REVIEW);
            }
            default -> reply = replyToUserService.replyMovie(message.getChatId(), message.getText());
        }
        return sendMsg(message.getChatId(), reply);
    }

    private SendMessage handleBotStateMessage(Message message) {
        String reply = null;
        BotState botState = userDataCache.getUserBotState(message.getFrom().getId());
        if (botState != null) {
            switch (botState) {
                case REVIEW -> {
                    if (message.getText().equals("/cancel")) {
                        userDataCache.deleteStateUser(message.getFrom().getId());
                        return sendMsg(message.getChatId(), "Жаль, что ты передумал(");
                    }
                    User user = userService.findUserByUserId(message.getFrom().getId());
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
                        reply = "Шось не то, прости пожалуйста((";
                        userDataCache.deleteStateUser(message.getFrom().getId());
                    }
                }
                case SEARCH -> reply = "Результаты поиска: ";
            }
        } else {
            reply = "Братик, звыняй, мои мозги пока пытаются обработать эту инфу, но шось не получается..";
        }
        return sendMsg(message.getChatId(), reply);
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
