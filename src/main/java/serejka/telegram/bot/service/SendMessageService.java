package serejka.telegram.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Service
public class SendMessageService {

    public SendMessage sendMsg(long chatId, String text) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text).setParseMode("html");
        return sendMessage;
    }

    public SendMessage sendMsg(long chatId, String text, InlineKeyboardMarkup inlineKeyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
        sendMessage.setChatId(chatId);
        sendMessage.setText(text).setParseMode("html");
        return sendMessage;
    }
}
