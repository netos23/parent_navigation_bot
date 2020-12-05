package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;

public interface InputMessageHandler {
    SendMessage handle(Message message);

    BotState getHandlerName();
}
