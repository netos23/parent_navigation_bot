package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.InputMessageHandler;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Component
public class SearchHandler implements InputMessageHandler {
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;
    private SearchingService searchingService;
    private Map<Integer, SearchItem> searchItemMap;
    private FutureSearchFinder finder;
    private ConcurrentLinkedQueue<ConcurrentItem> contentQueue;


    public SearchHandler(
            UserDataCache userDataCache,
            ReplyMessagesService messagesService,
            SearchingService searchingService
    ) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
        this.searchingService = searchingService;

        searchItemMap = new HashMap<>();
    }

    @Override
    public BotApiMethod<?> handle(Message message,String text) {
        return processUserInput(message,text);
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery callbackQuery) {
        return null;
    }

    @Override
    public boolean acceptsCallbackQueries() {
        return false;
    }

    private SendMessage processUserInput(Message message, String messageText) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();
        SendMessage replyToUser;

        switch (userDataCache.getUserCurrentBotState(userId)) {
            case SEARCH:
                replyToUser = sendStatusUpdate(message, "reply.fromInvite", BotState.SEARCH_GET_FROM);
                break;

            case SEARCH_GET_FROM:
                searchItemMap.put(userId, new SearchItem());
                if (validateInput(messageText)
                        && setFrom(userId, messageText)) {
                    replyToUser = sendStatusUpdate(message, "reply.toInvite", BotState.SEARCH_GET_TO);
                } else {
                    replyToUser = breakSearch(userId, message);
                }
                break;

            case SEARCH_GET_TO:
                if (validateInput(messageText)
                        && setTo(userId, messageText)) {
                    try {
                        beginAsyncSearch(userId, chatId);
                        replyToUser = sendStatusUpdate(message, "reply.searchBegin", BotState.PROCESSING);
                    } catch (Exception ex) {
                        log.error(ex.getMessage());
                        replyToUser = breakSearch(userId, message);
                    }
                } else {
                    replyToUser = breakSearch(userId, message);
                }
                break;

            case PROCESSING:
                replyToUser = messagesService.getReplyMessage(chatId, "reply.searchBegin");
                break;

            default:
                replyToUser = breakSearch(userId, message);
        }
        return replyToUser;
    }

    private boolean validateInput(String text) {
        return searchingService.hasName(text);
    }

    @Override
    public BotState[] getHandlerName() {
        return new BotState[]{
                BotState.SEARCH,
                BotState.SEARCH_GET_FROM,
                BotState.SEARCH_GET_TO
        };
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {
        this.contentQueue = contentQueue;
    }

    private SendMessage breakSearch(int userId, Message message) {
        searchItemMap.remove(userId);
        return sendStatusUpdate(message, "reply.errorInvite", BotState.IDLE);
    }

    private boolean setFrom(int userId, String from) {
        SearchItem searchItem = searchItemMap.get(userId);
        if (searchItem != null) {
            searchItem.setFrom(from);
            return true;
        }

        return false;
    }

    private boolean setTo(int userId, String to) {
        SearchItem searchItem = searchItemMap.get(userId);
        if (searchItem != null) {
            searchItem.setTo(to);
            return true;
        }

        return false;
    }

    private void beginAsyncSearch(int userId, long chatId) {
        // todo: проверка инициализации очереди
        this.finder = new FutureSearchFinder(searchingService, userDataCache, contentQueue);
        finder.setParams(userId, chatId, searchItemMap.get(userId));
        Thread thread = new Thread(finder);
        thread.start();
    }

    private void beginSearch(int userId, long chatId) {
        // todo: проверка инициализации очереди
        try {
            SearchItem currentSearch = searchItemMap.get(userId);
            SendPhoto[] results = searchingService
                    .getPath(currentSearch.getFrom(), currentSearch.getTo());
            if (results != null) {
                for (SendPhoto photo : results) {
                    photo.setChatId(chatId).setCaption("");
                    //todo: отправка в очередь
                }
            } else {
                throw new Exception("Error while searching");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        userDataCache.setUserCurrentBotState(userId, BotState.IDLE);
    }


    private SendMessage sendStatusUpdate(Message message, String replyMessage, BotState botState) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        userDataCache.setUserCurrentBotState(userId, botState);
        return messagesService.getReplyMessage(chatId, replyMessage);
    }
}
