package serejka.telegram.bot.logic.commands;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import serejka.telegram.bot.logic.enums.CallbackCommands;

public interface CallbackCommand {

    SendMessage generateMessage(CallbackQuery callbackQuery, String data);
    CallbackCommands getMyCommand();
}
