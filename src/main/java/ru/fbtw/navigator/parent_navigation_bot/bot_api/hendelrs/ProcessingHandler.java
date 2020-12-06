package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class ProcessingHandler implements InputMessageHandler {
    private final ReplyMessagesService messagesService;

    public ProcessingHandler(ReplyMessagesService messagesService) {
        this.messagesService = messagesService;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUserInput(message);
    }

    private SendMessage processUserInput(Message message) {
        long chatId = message.getChatId();
        return messagesService.getReplyMessage(chatId, "reply.searchBegin");
    }

    @Override
    public BotState[] getHandlerName() {
        return new BotState[]{BotState.PROCESSING};
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {

    }
}
