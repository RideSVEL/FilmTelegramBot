package serejka.telegram.bot.botapi;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

public class Bot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUsername;
    private String botToken;

    private final Facade facade;

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
}
