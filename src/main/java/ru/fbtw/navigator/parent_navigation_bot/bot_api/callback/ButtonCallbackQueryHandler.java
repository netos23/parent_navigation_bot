package ru.fbtw.navigator.parent_navigation_bot.bot_api.callback;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;

public interface ButtonCallbackQueryHandler {
    String getData();
    BotApiMethod<?> handle(CallbackQuery buttonQuery);
}
