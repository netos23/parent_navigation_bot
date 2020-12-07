package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.search;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.InputMessageHandler;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.search.MessagePredictorService;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class SmartSearchHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private ConcurrentLinkedQueue<ConcurrentItem> queue;
    private MessagePredictorService predictorService;
    private SearchingService searchingService;
    private FutureSearchFinder finder;

    public SmartSearchHandler(
            UserDataCache userDataCache,
            ReplyMessagesService messagesService,
            MessagePredictorService predictorService,
            SearchingService searchingService
    ) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.predictorService = predictorService;
        this.searchingService = searchingService;
    }

    @Override
    public BotApiMethod<?> handle(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyToUser;

        List<String> targets = predictorService.splitMessage(message.getText());

        if (targets.size() == 2) {
            replyToUser = sendStatusUpdate(message, "reply.searchBegin", BotState.PROCESSING);
            beginAsyncSearch(userId,chatId,targets.get(0),targets.get(1));
        } else {
            replyToUser = sendStatusUpdate(message, "reply.unknown", BotState.SEARCH_GET_FROM);
        }

        return replyToUser;
    }

    private void beginAsyncSearch(int userId, long chatId, String from, String to) {
        this.finder = new FutureSearchFinder(searchingService, userDataCache, queue);
        finder.setParams(userId, chatId, from, to);
        Thread thread = new Thread(finder);
        thread.start();
    }

    @Override
    public BotState[] getHandlerName() {
        return new BotState[]{BotState.SMART_SEARCH};
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {
        this.queue = contentQueue;
    }

    private SendMessage sendStatusUpdate(Message message, String replyMessage, BotState botState) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        userDataCache.setUserCurrentBotState(userId, botState);
        return messagesService.getReplyMessage(chatId, replyMessage);
    }
}
