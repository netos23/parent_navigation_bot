package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.service.ReplyMessagesService;

@Component
public class SearchHandler implements InputMessageHandler{
    private UserDataCache userDataCache;
    private ReplyMessagesService messagesService;


    public SearchHandler(UserDataCache userDataCache,
                          ReplyMessagesService messagesService) {
        this.userDataCache = userDataCache;
        this.messagesService = messagesService;
    }
    @Override
    public SendMessage handle(Message message) {
        int userId = message.getFrom().getId();
        long chatId = message.getChatId();

        SendMessage replyToUser = messagesService.getReplyMessage(chatId,"reply.help");
        userDataCache.setUserCurrentBotState(userId,BotState.IDLE);

        return replyToUser;
    }

    @Override
    public BotState getHandlerName() {
        return BotState.SMART_SEARCH;
    }
}
