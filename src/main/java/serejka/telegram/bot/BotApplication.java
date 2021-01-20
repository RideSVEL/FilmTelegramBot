package serejka.telegram.bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.io.File;
import java.io.IOException;


@SpringBootApplication
public class BotApplication {

	public static void main(String[] args) throws IOException {
		ApiContextInitializer.init();
		SpringApplication.run(BotApplication.class, args);
	}

}
