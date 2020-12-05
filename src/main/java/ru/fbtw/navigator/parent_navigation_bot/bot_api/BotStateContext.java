package ru.fbtw.navigator.parent_navigation_bot.bot_api;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.InputMessageHandler;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class BotStateContext {
    private final Map<BotState, InputMessageHandler> messageHandlers;

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers.stream()
                .collect(Collectors.toMap(
                        InputMessageHandler::getHandlerName,
                        handler -> handler,
                        (prev, cur) -> prev));
    }

    public SendMessage processInputMessage(BotState botState, Message message) {
        InputMessageHandler currentHandler = messageHandlers.get(botState);
        return currentHandler.handle(message);
    }
}
