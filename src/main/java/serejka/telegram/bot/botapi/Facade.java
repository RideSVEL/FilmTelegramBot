package serejka.telegram.bot.botapi;

import org.slf4j.Logger;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import serejka.telegram.bot.models.Movie;
import serejka.telegram.bot.service.ParserService;
import serejka.telegram.bot.service.ReplyToUserService;
import serejka.telegram.bot.service.StatisticsService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


@Component
public class Facade {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Facade.class);

    private final ReplyToUserService replyToUserService;
    private final ParserService parserService;
    private final StatisticsService statisticsService;

    public Facade(ReplyToUserService replyToUserService, ParserService parserService, StatisticsService statisticsService) {
        this.replyToUserService = replyToUserService;
        this.parserService = parserService;
        this.statisticsService = statisticsService;
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
            reply = handleInputMessage(message);
        }
        return reply;
    }

    private SendMessage handleInputMessage(Message message) throws IOException {
        String reply;
        switch (message.getText()) {
            case "/start" -> {
                reply = replyToUserService.replyStart(message);
                //statisticsService.updateCountCommand(1);
            }
            case "/help" -> {
                reply = "Я тебе всегда помогу!";
               // statisticsService.updateCountCommand(3);
            }
            case "Привет" -> reply = "И снова мы здороваемся!";
            case "/top" -> {
                List<Movie> movies = parserService.getListMovies();
                reply = replyToUserService.replyListMovies(message.getChatId(), movies);
                //statisticsService.updateCountCommand(2);
                return sendMsg(message.getChatId(), reply, getInlineMessageButtons(movies));
            }
            default -> reply = replyToUserService.replyMovie(message.getChatId(), message.getText());
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
