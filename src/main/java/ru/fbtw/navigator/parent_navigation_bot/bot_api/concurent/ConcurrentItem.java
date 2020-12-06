package ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent;

import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
public class ConcurrentItem {
    private List<SendPhoto> content;

    public ConcurrentItem(SendPhoto[] content) {
        this.content = new ArrayList<>();
        this.content.addAll(Arrays.asList(content));
    }
}
