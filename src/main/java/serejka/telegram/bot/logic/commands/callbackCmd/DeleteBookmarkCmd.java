package serejka.telegram.bot.logic.commands.callbackCmd;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import serejka.telegram.bot.logic.commands.CallbackCommand;
import serejka.telegram.bot.logic.enums.CallbackCommands;
import serejka.telegram.bot.service.BookmarkService;
import serejka.telegram.bot.service.SendMessageService;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class DeleteBookmarkCmd implements CallbackCommand {

    SendMessageService sendMsg;
    BookmarkService bookmarkService;

    @Override
    public SendMessage generateMessage(CallbackQuery callbackQuery, String data) {
        return sendMsg.sendMsg(callbackQuery.getFrom().getId(),
                bookmarkService.deleteBookmark(callbackQuery.getFrom().getId(), Long.parseLong(data)));
    }

    @Override
    public CallbackCommands getMyCommand() {
        return CallbackCommands.DELETE_BOOKMARK;
    }
}
