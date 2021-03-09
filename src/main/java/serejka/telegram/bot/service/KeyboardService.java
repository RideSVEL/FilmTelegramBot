package serejka.telegram.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import serejka.telegram.bot.logic.enums.Commands;
import serejka.telegram.bot.logic.enums.KeyboardCommands;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    public SendMessage getKeyboard(long chatId, String text, Commands command) {
        return command.equals(Commands.REVIEW) || command.equals(Commands.SEARCH)
                ? sendKeyboardWithMessage(chatId, text, createReturnKeyboard())
                : sendKeyboardWithMessage(chatId, text, createMainKeyboard());
    }

    private ReplyKeyboardMarkup createReturnKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Вернуться\uD83D\uDE15"));
        keyboard.add(row1);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private ReplyKeyboardMarkup createReplyMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

    public ReplyKeyboardMarkup createMainKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton(KeyboardCommands.TOPDAY.getValue()));
        row1.add(new KeyboardButton(KeyboardCommands.TOPWEEK.getValue()));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton(KeyboardCommands.TOP.getValue()));
        row2.add(new KeyboardButton(KeyboardCommands.HELP.getValue()));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton(KeyboardCommands.RANDOM.getValue()));
        KeyboardRow row4 = new KeyboardRow();
        row4.add(new KeyboardButton(KeyboardCommands.SEARCH.getValue()));
        row4.add(new KeyboardButton(KeyboardCommands.REVIEW.getValue()));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
        keyboard.add(row4);
        replyKeyboardMarkup.setKeyboard(keyboard);
        return replyKeyboardMarkup;
    }

    private SendMessage sendKeyboardWithMessage(
            long chatId, String text, ReplyKeyboardMarkup replyKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setParseMode("html");
        sendMessage.setChatId(chatId);
        sendMessage.setText(text);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    public InlineKeyboardMarkup getInlineMessageButtonForFilm(Integer movie_id) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton button;
        List<InlineKeyboardButton> keyboardButtons = new ArrayList<>();
        button = new InlineKeyboardButton();
        button.setText("Добавить в закладки✅");
        button.setCallbackData("bookmark=" + movie_id);
        keyboardButtons.add(button);
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtons);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
