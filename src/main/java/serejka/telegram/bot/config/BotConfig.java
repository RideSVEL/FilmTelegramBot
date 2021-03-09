package serejka.telegram.bot.config;

import info.movito.themoviedbapi.TmdbApi;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import serejka.telegram.bot.logic.bot.Bot;
import serejka.telegram.bot.logic.bot.Facade;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class BotConfig {
    private String webHookPath;
    private String botUsername;
    private String botToken;

    @Bean
    public Bot SuperBot(Facade facade) {
        DefaultBotOptions options = ApiContext.getInstance(DefaultBotOptions.class);
        Bot superBot = new Bot(options, facade);
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

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }

    @Bean
    public TmdbApi tmdbApi() {
        return new TmdbApi("948e6670159df009cc3be4b3cbab0697");
    }


}
