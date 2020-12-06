package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WelcomeHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public WelcomeHandler(UserDataCache userDataCache,
                          ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUserInput(message);
    }

    private SendMessage processUserInput(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.welcomeMessage");
        userDataCache.setUserCurrentBotState(userId, BotState.IDLE);
        return replyToUser;
    }

    @Override
    public BotState[] getHandlerName() {
        return new BotState[]{BotState.WELCOME};
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {

    }
}
