package serejka.telegram.bot.service;

import org.slf4j.Logger;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.ActionType;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.media.InputMediaPhoto;
import serejka.telegram.bot.botapi.Bot;
import serejka.telegram.bot.config.APIConfig;
import serejka.telegram.bot.models.Movie;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyToUserService {

    private final ParserService parserService;
    private final Bot superBot;

    public ReplyToUserService(ParserService parserService, @Lazy Bot superBot) {
        this.parserService = parserService;
        this.superBot = superBot;
    }

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(ReplyToUserService.class);

    public String replyMovie(Message message) {
        String reply;
        try {
            Movie movie = parserService.parseMovie(Integer.parseInt(message.getText()));
            reply = "Trunk Trunk Trunk";
            if (movie != null) {
                superBot.sendChatActionUpdate(message, ActionType.UPLOADPHOTO);
                log.info("Get movie: {}", movie.toString());
                List<InputMediaPhoto> list = new ArrayList<>();
                for (String s : movie.getPathToImages()) {
                    list.add(new InputMediaPhoto().setMedia(APIConfig.getPathToImage(s)));
                }
                superBot.sendMediaGroup(message, list);
                superBot.sendChatActionUpdate(message, ActionType.TYPING);
                reply = showMovie(movie);
            }
        } catch (NumberFormatException e) {
            reply = "Trunk Trunk Trunk";
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
        sb.append("\nПремьера: ").append(movie.getPremiere());
        sb.append("\nБюджет: ").append(movie.getBudget()).append("$");
        sb.append("\nПродолжительность: ").append(movie.getRuntime()).append(" мин.");
        sb.append("\nОригинальное навание: ").append(movie.getOriginalTitle());
        sb.append("\n\n").append(movie.getOverview());
        return sb.toString();
    }


}



