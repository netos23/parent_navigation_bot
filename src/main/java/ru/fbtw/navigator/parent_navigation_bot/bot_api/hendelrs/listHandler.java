package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;
import ru.fbtw.navigator.parent_navigation_bot.service.LocaleMessageService;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class listHandler implements InputMessageHandler {
    private SearchingService searchingService;
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;

    public listHandler(
            SearchingService searchingService,
            UserDataCache userDataCache,
            ReplyMessagesService messagesService
    ) {
        this.searchingService = searchingService;
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

        SendMessage replyToUser = messagesService.getReplyUnLocaledMessage(chatId, buildMessage());
        userDataCache.setUserCurrentBotState(userId, BotState.IDLE);
        return replyToUser;
    }


    @Override
    public BotState[] getHandlerName() {
        return new BotState[]{BotState.LIST};
    }

    @Override
    public void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue) {

    }

    private String buildMessage() {
        LocaleMessageService localeMessageService
                = messagesService.getLocaleMessageService();

        StringBuilder builder = new StringBuilder(localeMessageService.getMessage("reply.list"));
        // add empty line after text for better performance
        builder.append('\n');

        for (String roomName : searchingService.getNamesSet()) {
            builder.append(roomName)
                    .append('\n');
        }

        return builder.toString();
    }


}
