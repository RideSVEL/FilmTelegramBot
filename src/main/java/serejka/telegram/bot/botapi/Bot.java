package serejka.telegram.bot.botapi;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendChatAction;
import org.telegram.telegrambots.meta.api.methods.send.SendMediaGroup;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class Bot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUsername;
    private String botToken;

    private final Facade facade;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        try {
            return facade.handle(update);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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

    public void sendMediaGroup(long chatId, List<InputMediaPhoto> inputMediaPhotos) {
        SendMediaGroup sendMediaGroup = new SendMediaGroup();
        sendMediaGroup.setMedia(Collections.unmodifiableList(inputMediaPhotos));
        sendMediaGroup.setChatId(chatId);
        try {
            execute(sendMediaGroup);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageByAdmin(long chatId, String reply) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setParseMode("html");
        sendMessage.setText(reply);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendChatActionUpdate(long chatId, ActionType type) {
        SendChatAction sendChatAction = new SendChatAction();
        sendChatAction.setChatId(chatId);
        sendChatAction.setAction(type);
        try {
            execute(sendChatAction);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
