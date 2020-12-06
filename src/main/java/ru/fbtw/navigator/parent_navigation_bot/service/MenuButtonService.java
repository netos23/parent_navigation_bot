package ru.fbtw.navigator.parent_navigation_bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuButtonService {
   private LocaleMessageService localeMessageService;

    public MenuButtonService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getMainMenuMessage(final long chatId, String localizedMessage){
        final ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();
        final SendMessage mainMenuMessage =
                createMessageWithKeyboard(
                        chatId, localeMessageService.getMessage(localizedMessage), replyKeyboardMarkup);

        return mainMenuMessage;
    }

    private SendMessage createMessageWithKeyboard(long chatId, String message, ReplyKeyboardMarkup replyKeyboardMarkup) {
        final SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        if (replyKeyboardMarkup != null) {
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
        }
        return sendMessage;
    }

    private ReplyKeyboardMarkup getMainMenuKeyboard() {
        //localize and replace with builder

        final ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboard = new ArrayList<>();

        KeyboardRow searchRow = new KeyboardRow();
        KeyboardRow listRow = new KeyboardRow();
        KeyboardRow helpRow = new KeyboardRow();

        searchRow.add(new KeyboardButton("/search"));
        searchRow.add(new KeyboardButton("/list"));
        searchRow.add(new KeyboardButton("/help"));

        keyboard.add(searchRow);
        keyboard.add(listRow);
        keyboard.add(helpRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

}
