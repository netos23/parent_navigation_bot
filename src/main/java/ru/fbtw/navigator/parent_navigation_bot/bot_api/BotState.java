package ru.fbtw.navigator.parent_navigation_bot.bot_api;

public enum BotState {
    WELCOME,
    PRINT_HELP,
    IDLE,
    SMART_SEARCH,
    VOICE_SEARCH,
    SEARCH,
    SEARCH_GET_FROM,
    SEARCH_GET_TO,
    LIST,
    CONFIRM_SMART_SEARCH,
    PROCESSING;

    @Deprecated
    public static BotState getOrDefault(int messageType) {
        if (messageType >= 0 && messageType < values().length) {
            return values()[messageType];
        } else {
            return WELCOME;
        }
    }
}
