package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class HelpHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public HelpHandler(UserDataCache userDataCache,
                       ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId, "reply.help");
        userDataCache.setUserCurrentBotState(userId, BotState.IDLE);

        return replyToUser;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public boolean acceptsCallbackQueries() {
        return false;
    }

    @Override
    public BotState[] getHandlerName() {
        return new BotState[]{BotState.PRINT_HELP};
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {

    }
}
