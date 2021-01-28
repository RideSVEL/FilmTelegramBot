package serejka.telegram.bot.logic;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum Commands {

    TOPDAY("/topday", KeyboardCommands.TOPDAY.getValue()), TOPWEEK("/topweek", KeyboardCommands.TOPWEEK.getValue()), TOP("/top", KeyboardCommands.TOP.getValue()),
    START("/start"), HELP("/help",KeyboardCommands.HELP.getValue()), OTHER("other"),
    REVIEW("/review",KeyboardCommands.REVIEW.getValue()), CANCEL("/cancel", KeyboardCommands.CANCEL.getValue()),
    SEARCH("/search", KeyboardCommands.SEARCH.getValue()), RANDOM("/random", KeyboardCommands.RANDOM.getValue());

    Commands(String... commands) {
        this.commandNames = new HashSet<>(Arrays.asList(commands));
    }

    private final Set<String> commandNames;

    private Boolean checkContainsCommand(String command) {
        return commandNames.contains(command);
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
