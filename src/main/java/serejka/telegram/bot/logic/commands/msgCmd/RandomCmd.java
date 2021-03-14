package serejka.telegram.bot.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.bot.Bot;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.logic.commands.MessageCommand;
import serejka.telegram.bot.service.MovieService;
import serejka.telegram.bot.service.ReplyToUserService;
import serejka.telegram.bot.service.SendMessageService;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RandomCmd implements MessageCommand {
    Bot superBot;
    MovieService movieService;
    SendMessageService sendMsg;
    ReplyToUserService replyToUserService;

    public RandomCmd(@Lazy Bot superBot, MovieService movieService,
                     SendMessageService sendMsg, ReplyToUserService replyToUserService) {
        this.superBot = superBot;
        this.movieService = movieService;
        this.sendMsg = sendMsg;
        this.replyToUserService = replyToUserService;
    }

    @Override
    public SendMessage generateMessage(Message message) {
        superBot.sendDiceForWaiting(message.getChatId());
        movieService.sendRandomMovie(message, superBot);
        return sendMsg.sendMsg(message.getFrom().getId(), replyToUserService.senWaiting(message));
    }

    @Override
    public Commands getMyCommand() {
        return Commands.RANDOM;
    }
}
