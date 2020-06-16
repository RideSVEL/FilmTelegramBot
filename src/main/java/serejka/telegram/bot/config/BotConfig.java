package serejka.telegram.bot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import serejka.telegram.bot.botAPI.Bot;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUsername;
    private String botToken;

    @Bean
    public Bot SuperBot() {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);

        Bot superBot = new Bot(options);
        superBot.setBotUsername(botUsername);
        superBot.setBotToken(botToken);
        superBot.setWebHookPath(webHookPath);
        return superBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

}
