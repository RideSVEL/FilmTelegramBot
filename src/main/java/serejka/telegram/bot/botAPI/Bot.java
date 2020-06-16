package serejka.telegram.bot.botAPI;

import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;

import serejka.telegram.bot.models.User;
import serejka.telegram.bot.repository.UserRepository;

public class Bot extends TelegramWebhookBot {

    private String webHookPath;
    private String botUsername;
    private String botToken;

    private Facade facade;

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
//        if (update.getMessage() != null && update.getMessage().hasText()) {
//            long chatId = update.getMessage().getChatId();
//            System.out.println(chatId);
//            try {
//                execute(new SendMessage(chatId, "Hi " + update.getMessage().getText()));
//            } catch (TelegramApiException e) {
//                e.printStackTrace();
//            }
//        }


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
