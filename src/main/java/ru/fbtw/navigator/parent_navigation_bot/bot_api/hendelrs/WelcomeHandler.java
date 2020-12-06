package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.service.MenuButtonService;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class WelcomeHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private MenuButtonService buttonService;
    private boolean isInitedKeyboard;

    public WelcomeHandler(
            UserDataCache userDataCache,
            ReplyMessagesService messagesService,
            MenuButtonService buttonService
    ) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.buttonService = buttonService;
        isInitedKeyboard = false;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        return processUserInput(message);
    }

    private SendMessage processUserInput(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyToUser;

        if (isInitedKeyboard) {
            replyToUser = messagesService.getReplyMessage(chatId, "reply.welcomeMessage");
        } else {
            replyToUser = buttonService.getMainMenuMessage(chatId,"reply.welcomeMessage");
            isInitedKeyboard = true;
        }

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
