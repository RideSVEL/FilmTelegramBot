package serejka.telegram.bot.logic.bot;

import lombok.SneakyThrows;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendDice;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import java.util.Collections;
import java.util.List;

public class Bot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUsername;
    private String botToken;

    private final Facade facade;

    @SneakyThrows
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        return facade.handle(update);
    }

    public Bot(DefaultBotOptions options, Facade facade) {
        super(options);
        this.facade = facade;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public String getBotPath() {
        return webHookPath;
    }

    public void setWebHookPath(String webHookPath) {
        this.webHookPath = webHookPath;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    @SneakyThrows
    public void sendMediaGroup(long chatId, List<InputMediaPhoto> inputMediaPhotos) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setMedia(Collections.unmodifiableList(inputMediaPhotos));
        sendMediaGroup.setChatId(String.valueOf(chatId));
            execute(sendMediaGroup);
    }

    @SneakyThrows
    public void sendMessageByAdmin(long chatId, String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setParseMode("html");
        sendMessage.setText(reply);
            execute(sendMessage);
    }

    @SneakyThrows
    public void sendMessageWithKeyboard(
            long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setParseMode("html");
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
            execute(sendMessage);

    }

    @SneakyThrows
    public void sendChatActionUpdate(long chatId, ActionType type) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(String.valueOf(chatId));
        sendChatAction.setAction(type);
            execute(sendChatAction);

    }

    @SneakyThrows
    public void sendDiceForWaiting(long chatId) {
        SendDice sendDice = new SendDice();
        sendDice.setChatId(chatId);
            execute(sendDice);
    }
}
