package serejka.telegram.bot.logic.enums;

public enum KeyboardCommands {
    TOPDAY("Новинки\uD83C\uDD95"), TOPWEEK("TOP Недели\uD83D\uDE0E"),
    TOP("TOP\uD83D\uDD25"), HELP("Помощь\uD83C\uDD98"),
    REVIEW("Оставить отзыв\uD83D\uDE4B\u200D♂️"), CANCEL("Вернуться\uD83D\uDE15"),
    SEARCH("Поиск\uD83D\uDD0D"), RANDOM("Кинорулетка\uD83C\uDFB2");

    private final String value;

    KeyboardCommands(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
