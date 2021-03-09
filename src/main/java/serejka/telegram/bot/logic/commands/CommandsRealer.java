package serejka.telegram.bot.logic.commands;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import serejka.telegram.bot.logic.enums.CallbackCommands;
import serejka.telegram.bot.logic.enums.Commands;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CommandsRealer {

    Map<Commands, MessageCommand> messageCommandMap;
    Map<CallbackCommands, CallbackCommand> callbackCommandMap;

    @Autowired
    public CommandsRealer(List<MessageCommand> messageCommands, List<CallbackCommand> callbackCommands) {
        messageCommandMap = messageCommands.stream()
                .collect(toMap(MessageCommand::getMyCommand, Function.identity()));
        callbackCommandMap = callbackCommands.stream()
                .collect(toMap(CallbackCommand::getMyCommand, Function.identity()));
    }

    public SendMessage answerOnUpdate(Message message) {
        Commands command = Commands.getName(message.getText());
        MessageCommand messageCommand = messageCommandMap.get(command);
        if (messageCommand == null) {
            log.error("Command not found - " + command);
            messageCommand = messageCommandMap.get(Commands.OTHER);
        }
        return messageCommand.generateMessage(message);
    }

    public SendMessage answerOnUpdate(CallbackQuery callbackQuery) {
        String[] data = callbackQuery.getData().split("=");
        log.info(callbackQuery.getData());
        CallbackCommands command = CallbackCommands.getName(data[0]);
        CallbackCommand callbackCommand = callbackCommandMap.get(command);
        if (callbackCommand == null) {
            log.error("Command not found - " + command);
            callbackCommand = callbackCommandMap.get(CallbackCommands.OTHER);
        }
        return callbackCommand.generateMessage(callbackQuery, data[1]);
    }
}
