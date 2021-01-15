package serejka.telegram.bot;

import org.h2.tools.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;

import java.sql.SQLException;


@SpringBootApplication
public class BotApplication {

	public static void main(String[] args) throws SQLException {
		ApiContextInitializer.init();
		SpringApplication.run(BotApplication.class, args);
	}

}
