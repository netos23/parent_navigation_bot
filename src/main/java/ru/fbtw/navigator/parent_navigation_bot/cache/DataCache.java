package ru.fbtw.navigator.parent_navigation_bot.cache;

import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;

public interface DataCache {
    void setUserCurrentBotState(int userId, BotState botState);

    BotState getUserCurrentBotState(int userId);

}
