package serejka.telegram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import serejka.telegram.bot.service.CopyService;

import java.io.File;
import java.io.IOException;


@SpringBootApplication
public class BotApplication {

	public static void main(String[] args) throws IOException {
		CopyService.copyFileUsingStream(new File("database/telegram.mv.db"),
				new File("database/telegram-copy.mv.db"));
		ApiContextInitializer.init();
		SpringApplication.run(BotApplication.class, args);
	}

}
