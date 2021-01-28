package serejka.telegram.bot.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import serejka.telegram.bot.logic.Bot;
import serejka.telegram.bot.logic.Commands;
import serejka.telegram.bot.config.APIConfig;
import serejka.telegram.bot.models.Movie;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReplyToUserService {

    ParserService parserService;
    Bot superBot;
    UserService userService;

    public ReplyToUserService(ParserService parserService, @Lazy Bot superBot, UserService userService) {
        this.parserService = parserService;
        this.superBot = superBot;
        this.userService = userService;
    }

    public String replyHelp() {
        return "Я тебе всегда помогу!";
    }

    public String replyStart(Message message) {
        userService.checkAndSave(message);
        return "Привет, " + message.getFrom().getFirstName()
                + "!\nТы попал в лучший бот по подбору фильмов :)" +
                "\nВсе необходимые функции ты сможешь найти на клавиатуре снизу" +
                "\nЕсли возникут вопросы, используй клавишу \"Помощь\", либо отправь команду /help" +
                "\nУдачки! \uD83D\uDE1C";
    }

    public String replyReview() {
        return "Я рад, что ты решил оставить отзыв о нашем боте," +
                " отправь свои пожелания\uD83D\uDE0C" +
                "\nЛибо можешь отменить операцию командой - /cancel\uD83D\uDE15";
    }

    public String replySearch() {
        return "Давай найдем нужный тебе фильм\uD83D\uDE09" +
                "\nПиши название, либо можешь отменить операцию нажав соответствующую клавишу на клавиаутре)";
    }

    public String replyError() {
        return "Что-то не так, извини\uD83D\uDE1E";
    }


    public String replyListMovies(List<Movie> movies, Commands commands) {
        String reply;
        reply = replyError();
        if (movies != null) {
            log.info("Get movie: {}", movies.toString());
            StringBuilder sb = new StringBuilder();
            if (commands == Commands.TOPDAY)
                sb.append("<em>Популярные фильмы на сегодня:</em>");
            if (commands == Commands.TOPWEEK)
                sb.append("<em>Лучшее за неделю:</em>");
            if (commands == Commands.TOP)
                sb.append("<em>Пользуются спросом большой промежуток времени:</em>");
            if (commands == Commands.SEARCH)
                sb.append("Если результаты отсутствуют или не удовлетворяют поиски, повтори ввод\uD83D\uDE09" +
                        "\nТакже можешь отменить операцию, нажав клавишу на клавиатуре" +
                        "\n\n<em>Нашлось:</em>");
            for (int i = 0; i < movies.size(); i++) {
                Movie movie = movies.get(i);
                sb.append("\n\n<b>").append(i + 1).append(". <em>").append(movie.getTitle()).append(" (")
                        .append(movie.getYear()).append(")</em></b>").append(" | ").append(movie.getVoteAverage()).append("\nЖанр: ");
                for (String genre : movie.getGenres()) {
                    sb.append(genre).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length() - 1);
            }
            sb.append("\n\nДля получения подробностей фильма воспользуйся одной из кнопок ниже:");
            return sb.toString();
        }
        return reply;
    }

    public String replyCheckMessageToUser(Message message) {
        superBot.sendChatActionUpdate(message.getChatId(), ActionType.TYPING);
        return "Данная функция находится на стадии тестирования, поэтому возможны небольшие задержки\uD83D\uDE22" +
                "\nНадо чуточку потерпеть\uD83D\uDC40";
    }


    public String replyMovie(long chatId, String filmId) {
        String reply;
        try {
            reply = "Что-то не получилось найти такой фильм...";
            Movie movie = parserService.parseMovie(Integer.parseInt(filmId));
            if (movie != null) {
                superBot.sendChatActionUpdate(chatId, ActionType.UPLOADPHOTO);
                log.info("Get movie: {}", movie.toString());
                List<InputMediaPhoto> list = new ArrayList<>();
                for (String s : movie.getPathToImages()) {
                    InputMediaPhoto inputMediaPhoto = new InputMediaPhoto();
                    inputMediaPhoto.setMedia(APIConfig.getPathToImage(s));
                    list.add(inputMediaPhoto);
                }
                superBot.sendMediaGroup(chatId, list);
                superBot.sendChatActionUpdate(chatId, ActionType.TYPING);
                reply = showMovie(movie);
            }
        } catch (NumberFormatException e) {
            reply = "Что-то не получилось найти такой фильм...";
        }
        return reply;
    }

    private String showMovie(Movie movie) {
        StringBuilder sb = new StringBuilder("<b><u>"
                + movie.getTitle() + " (" + movie.getYear() + ")" + "</u></b>\n\n");
        sb.append("Страна: ");
        for (String s : movie.getCountry()) {
            sb.append(s).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append("\n<em>Оценка: ").append(movie.getVoteAverage()).append(" (")
                .append(movie.getVotes()).append(")</em>");
        sb.append("\nЖанр: ");
        for (String s : movie.getGenres()) {
            sb.append(s).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length() - 1);
        sb.append("\nПремьера: ").append(movie.getReleaseDate());
        sb.append("\nБюджет: ").append(movie.getBudget()).append("$");
        sb.append("\nПродолжительность: ").append(movie.getRuntime()).append(" мин.");
        sb.append("\nОригинальное навание: ").append(movie.getOriginalTitle());
        sb.append("\n\n").append(movie.getOverview());
        return sb.toString();
    }


}



