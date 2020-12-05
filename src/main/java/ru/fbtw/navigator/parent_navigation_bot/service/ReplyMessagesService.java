package ru.fbtw.navigator.parent_navigation_bot.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Getter
@Service
public class ReplyMessagesService {
    private LocaleMessageService localeMessageService;

    public ReplyMessagesService(LocaleMessageService localeMessageService) {
        this.localeMessageService = localeMessageService;
    }

    public SendMessage getReplyMessage(long chatId, String replyMessage){
        return new SendMessage(chatId,localeMessageService.getMessage(replyMessage));
    }
    public SendMessage getReplyUnLocaledMessage(long chatId, String replyMessage){
        return new SendMessage(chatId,replyMessage);
    }
}
