package serejka.telegram.bot.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import serejka.telegram.bot.botapi.Bot;
import serejka.telegram.bot.botapi.Commands;
import serejka.telegram.bot.config.APIConfig;
import serejka.telegram.bot.models.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ReplyToUserService {

    private final ParserService parserService;
    private final Bot superBot;
    private final UserService userService;

    public ReplyToUserService(ParserService parserService, @Lazy Bot superBot, UserService userService) {
        this.parserService = parserService;
        this.superBot = superBot;
        this.userService = userService;
    }

    public String replyStart(Message message) {
        userService.checkAndSave(message);
        return "Привет, " + message.getFrom().getFirstName() + "!\n Давай пообщаемся! Как у тебя дела?";
    }

    public String replyListMovies(List<Movie> movies, Commands commands) {
        String reply;
        reply = "Блин братан, шось не то, звыняй";
        if (movies != null) {
            log.info("Get movie: {}", movies.toString());
            StringBuilder sb = new StringBuilder();
            if (commands == Commands.TOPDAY)
                sb.append("<em>Популярные фильмы на сегодня:</em>");
            if (commands == Commands.TOPWEEK)
                sb.append("<em>Лучшее за неделю:</em>");
            if (commands == Commands.TOP)
                sb.append("<em>Пользуются спросом большой промежуток времени:</em>");
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


    public String replyMovie(long chatId, String filmId) {
        String reply;
        try {
            Movie movie = parserService.parseMovie(Integer.parseInt(filmId));
            reply = "Братан, я пока не умею отвечать на такие сообщения\n" +
                    "Надо чуточку потерпеть..";
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
            reply = "Братан, я пока не умею отвечать на такие сообщения\n" +
                    "Надо чуточку потерпеть..";
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



