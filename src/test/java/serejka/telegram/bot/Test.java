package serejka.telegram.bot;

import org.springframework.boot.test.context.TestComponent;
import serejka.telegram.bot.service.ParserService;

@TestComponent
public class Test {

    public static void main(String[] args) {

        System.out.println(new ParserService(superBot).parseMovie(15472));
    }
}
