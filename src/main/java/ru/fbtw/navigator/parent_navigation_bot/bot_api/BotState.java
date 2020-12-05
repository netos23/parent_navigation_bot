package ru.fbtw.navigator.parent_navigation_bot.bot_api;

public enum BotState {
    WELCOME,
    PRINT_HELP,
    IDLE,
    SEARCH,
    PROCESSING;

    public static BotState getOrDefault(int messageType) {
        if (messageType >= 0 && messageType < values().length) {
            return values()[messageType];
        } else {
            return WELCOME;
        }
    }
}
