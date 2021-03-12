package serejka.telegram.bot.logic.enums;

public enum CallbackCommands {

    MOVIE("movie"), BOOKMARK("bookmark"), OTHER("other");

    private final String value;

    CallbackCommands(String value) {
        this.value = value;
    }

    public static CallbackCommands getName(String data) {
        for (CallbackCommands commands : values()) {
            if (commands.value.equals(data)) {
                return commands;
            }
        }
        return OTHER;
    }

}
