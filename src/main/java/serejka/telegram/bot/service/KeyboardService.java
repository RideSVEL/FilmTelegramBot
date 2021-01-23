package serejka.telegram.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import serejka.telegram.bot.botapi.Commands;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeyboardService {

    public SendMessage getMainKeyboard(long chatId, String text, Commands command) {
        return command.equals(Commands.REVIEW)
                ? sendKeyboardWithMessage(chatId, text, createReviewKeyboard())
                : sendKeyboardWithMessage(chatId, text, createMainKeyboard());
    }

    private ReplyKeyboardMarkup createReviewKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Отменить\uD83D\uDE15"));
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

    private ReplyKeyboardMarkup createMainKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = createReplyMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Новинки\uD83C\uDD95"));
        row1.add(new KeyboardButton("TOP Недели\uD83D\uDE0E"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("TOP\uD83D\uDD25"));
        row2.add(new KeyboardButton("Помощь\uD83C\uDD98"));
        KeyboardRow row3 = new KeyboardRow();
        row3.add(new KeyboardButton("Оставить отзыв\uD83D\uDE4B\u200D♂️"));
        keyboard.add(row1);
        keyboard.add(row2);
        keyboard.add(row3);
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
}
