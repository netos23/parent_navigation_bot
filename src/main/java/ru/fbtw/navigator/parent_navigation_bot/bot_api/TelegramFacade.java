package ru.fbtw.navigator.parent_navigation_bot.bot_api;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.AsyncMessageSender;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;

import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@Slf4j
public class TelegramFacade {
    private final String START = "/start";
    private final String HELP = "/help";
    private final String LIST = "/list";

    private BotStateContext botStateContext;
    private UserDataCache userDataCache;
    private MapperTelegramBot mapperTelegramBot;
    private ConcurrentLinkedQueue<ConcurrentItem> queue;


    public TelegramFacade(
            BotStateContext botStateContext,
            UserDataCache userDataCache
    ) {
        this.botStateContext = botStateContext;
        queue = botStateContext.getContentQueue();
        this.userDataCache = userDataCache;
    }

    public BotApiMethod<?> handleUpdate(Update update) {
        BotApiMethod<?> replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {}, text:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());

            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private BotApiMethod<?> handleInputMessage(Message message) {
        String inputText = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        BotApiMethod<?> replyMessage;

        botState = userDataCache.getUserCurrentBotState(userId);

        if (botState == BotState.IDLE) {
            switch (inputText) {
                case START:
                    botState = BotState.WELCOME;
                    break;
                case HELP:
                    botState = BotState.PRINT_HELP;
                    break;
                case LIST:
                    botState = BotState.LIST;
                    break;
                default:
                    botState = BotState.SEARCH;
            }
        }

        userDataCache.setUserCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }

    public void setTelegramBot(MapperTelegramBot mapperTelegramBot) {
        this.mapperTelegramBot = mapperTelegramBot;
        AsyncMessageSender asyncMessageSender
                = new AsyncMessageSender(mapperTelegramBot,queue);
        asyncMessageSender.start();
    }
}
