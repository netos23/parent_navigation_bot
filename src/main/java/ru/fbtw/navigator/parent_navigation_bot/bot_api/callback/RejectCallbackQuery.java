package ru.fbtw.navigator.parent_navigation_bot.bot_api.callback;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public class RejectCallbackQuery implements ButtonCallbackQueryHandler{

    @Override
    public String getData() {
        return null;
    }

    @Override
    public BotApiMethod<?> handle(CallbackQuery buttonQuery) {
        return null;
    }
}
