package serejka.telegram.bot.botapi;

public enum Commands {
    TOPDAY("/topday"), TOPWEEK("/topweek"), TOP("/top"), START("/start"), HELP("/help");

    Commands(String command) {
        this.command = command;
    }

    private final String command;

    public final String getCommand() {
        return command;
    }

//    public static Commands getName(String command) {
//        for (Commands commands : Commands.values()) {
//            if (commands.getCommand().equals(command)) {
//                return commands;
//            }
//        }
//        return null;
//    }


}
