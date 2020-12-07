package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;

import java.util.concurrent.ConcurrentLinkedQueue;

public interface InputMessageHandler {
    BotApiMethod<?> handle(Message message);
    BotApiMethod<?> handle(CallbackQuery callbackQuery);

    boolean acceptsCallbackQueries();

    BotState[] getHandlerName();

    void setQueue(ConcurrentLinkedQueue<ConcurrentItem> contentQueue);
}
