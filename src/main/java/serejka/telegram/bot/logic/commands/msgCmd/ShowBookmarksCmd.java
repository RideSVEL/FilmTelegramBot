package serejka.telegram.bot.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.commands.MessageCommand;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.models.Movie;
import serejka.telegram.bot.service.BookmarkService;
import serejka.telegram.bot.service.MovieService;
import serejka.telegram.bot.service.ReplyToUserService;
import serejka.telegram.bot.service.SendMessageService;

import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ShowBookmarksCmd implements MessageCommand {

    BookmarkService bookmarkService;
    MovieService movieService;
    ReplyToUserService replyService;
    SendMessageService messageService;

    @Override
    public SendMessage generateMessage(Message message) {
        List<Movie> movies = bookmarkService
                .findAllMoviesByUserBookmarks(message.getFrom().getId());
        Collections.reverse(movies);
        return messageService.sendMsg(message.getFrom().getId(),
                replyService.replyListMovies(movies, getMyCommand()),
                movieService.getInlineMessageButtons(movies, true));
    }

    @Override
    public Commands getMyCommand() {
        return Commands.BOOKMARKS;
    }
}
