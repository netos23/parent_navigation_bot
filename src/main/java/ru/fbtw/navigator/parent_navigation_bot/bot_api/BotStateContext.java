package ru.fbtw.navigator.parent_navigation_bot.bot_api;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.InputMessageHandler;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

@Component
public class BotStateContext {
    private final Map<BotState, InputMessageHandler> messageHandlers;
    private ConcurrentLinkedQueue<ConcurrentItem> contentQueue;

    public BotStateContext(List<InputMessageHandler> messageHandlers) {
        contentQueue = new ConcurrentLinkedQueue<>();
        this.messageHandlers = messageHandlers.stream()
                .flatMap(handler -> Arrays.stream(handler.getHandlerName())
                        .collect(Collectors.toMap(val -> val, val -> handler, (prev, next) -> prev))
                        .entrySet()
                        .stream())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (prev, next) -> prev));

        messageHandlers.forEach(handler -> handler.setQueue(contentQueue));
    }

    public BotApiMethod<?> processInputMessage(BotState botState, Message message, String text) {
        InputMessageHandler currentHandler = messageHandlers.get(botState);
        return currentHandler.handle(message,text);
    }

    public ConcurrentLinkedQueue<ConcurrentItem> getContentQueue() {
        return contentQueue;
    }

    public boolean acceptsCallbackQueries(BotState botState) {
        InputMessageHandler handler = messageHandlers.get(botState);
        return handler != null && handler.acceptsCallbackQueries();
    }

    public BotApiMethod<?> processCallbackQuery(BotState botState, CallbackQuery query) {
        InputMessageHandler currentHandler = messageHandlers.get(botState);
        return currentHandler.handle(query);
    }
}
