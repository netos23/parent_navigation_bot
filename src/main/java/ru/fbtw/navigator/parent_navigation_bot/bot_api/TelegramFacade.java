package ru.fbtw.navigator.parent_navigation_bot.bot_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;

@Component
@Slf4j
public class TelegramFacade {
    private final String START = "/start";
    private final String HELP = "/help";

    private BotStateContext botStateContext;
    private UserDataCache userDataCache;


    public TelegramFacade(
            BotStateContext botStateContext,
            UserDataCache userDataCache
    ) {
        this.botStateContext = botStateContext;
        this.userDataCache = userDataCache;
    }

    public SendMessage handleUpdate(Update update) {
        SendMessage replyMessage = null;

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            log.info("New message from User:{}, chatId: {}, text:{}",
                    message.getFrom().getUserName(), message.getChatId(), message.getText());

            replyMessage = handleInputMessage(message);
        }
        return replyMessage;
    }

    private SendMessage handleInputMessage(Message message) {
        String inputText = message.getText();
        int userId = message.getFrom().getId();
        BotState botState;
        SendMessage replyMessage;

        botState = userDataCache.getUserCurrentBotState(userId);

        if (botState == BotState.IDLE) {
            switch (inputText) {
                case START:
                    botState = BotState.WELCOME;
                    break;
                case HELP:
                    botState = BotState.PRINT_HELP;
                    break;
                default:
                    botState = BotState.SEARCH;
            }
        }

        userDataCache.setUserCurrentBotState(userId, botState);
        replyMessage = botStateContext.processInputMessage(botState, message);

        return replyMessage;
    }
}
