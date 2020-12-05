package ru.fbtw.navigator.parent_navigation_bot.cache;

import org.springframework.stereotype.Component;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserDataCache implements DataCache {
    private Map<Integer, BotState> userBotStates;

    public UserDataCache() {
        userBotStates = new HashMap<>();
    }

    @Override
    public void setUserCurrentBotState(int userId, BotState botState) {
        userBotStates.put(userId, botState);
    }

    @Override
    public BotState getUserCurrentBotState(int userId) {
        BotState botState = userBotStates.get(userId);
        if (botState == null) {
            botState = BotState.IDLE;
        }
        return botState;
    }
}
