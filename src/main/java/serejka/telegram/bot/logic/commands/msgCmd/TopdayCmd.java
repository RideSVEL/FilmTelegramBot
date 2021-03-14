package serejka.telegram.bot.logic.commands.msgCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.logic.commands.MessageCommand;
import serejka.telegram.bot.service.MovieService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TopdayCmd implements MessageCommand {

    MovieService movieService;

    @Override
    public SendMessage generateMessage(Message message) {
        return movieService.sendListMovies(Commands.TOPDAY, message);
    }

    @Override
    public Commands getMyCommand() {
        return Commands.TOPDAY;
    }
}
