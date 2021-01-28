package serejka.telegram.bot.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Commands {

    TOPDAY("/topday", "Новинки\uD83C\uDD95"), TOPWEEK("/topweek", "TOP Недели\uD83D\uDE0E"), TOP("/top", "TOP\uD83D\uDD25"),
    START("/start"), HELP("/help", "Помощь\uD83C\uDD98"), OTHER("other"),
    REVIEW("/review", "Оставить отзыв\uD83D\uDE4B\u200D♂️"), CANCEL("/cancel", "Вернуться\uD83D\uDE15"),
    SEARCH("/search", "Поиск\uD83D\uDD0D"), RANDOM("/random");

    Commands(String... commands) {
        firstCommandName = commands[0];
        this.commandNames = new HashSet<>(Arrays.asList(commands));
    }

    private final Set<String> commandNames;
    private final String firstCommandName;

    private Boolean checkContainsCommand(String command) {
        return commandNames.contains(command);
    }

    public final String getFirstCommandName() {
        return firstCommandName;
    }

    public static Commands getName(String command) {
        for (Commands commands : Commands.values()) {
            if (commands.checkContainsCommand(command)) {
                return commands;
            }
        }
        return OTHER;
    }

}
