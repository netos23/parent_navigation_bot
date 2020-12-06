package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.search;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.BotState;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.MapperTelegramBot;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent.ConcurrentItem;
import ru.fbtw.navigator.parent_navigation_bot.cache.UserDataCache;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j

public class FutureSearchFinder implements Runnable{
 //   private MapperTelegramBot telegramBot;
    private SearchingService searchingService;
    private UserDataCache userDataCache;
    private int userId;
    private long chatId;
    private SearchItem currentSearch;
    private ConcurrentLinkedQueue<ConcurrentItem> concurrentItems;

    public FutureSearchFinder(
           // @Lazy MapperTelegramBot telegramBot,
            SearchingService searchingService,
            UserDataCache userDataCache,
           ConcurrentLinkedQueue<ConcurrentItem> queue
    ) {
        concurrentItems = queue;
  //      this.telegramBot = telegramBot;
        this.searchingService = searchingService;
        this.userDataCache = userDataCache;
    }

    public void setParams(int userId, long chatId, SearchItem currentSearch){
        this.userId = userId;
        this.chatId = chatId;
        this.currentSearch = currentSearch;
    }


    @Override
    public void run() {
        try {
            SendPhoto[] results = searchingService
                    .getPath(currentSearch.getFrom(), currentSearch.getTo());
            if(results != null) {
                for (SendPhoto photo : results) {
                    photo.setChatId(chatId).setCaption("");
                }
                concurrentItems.add(new ConcurrentItem(results));
            }else{
                throw new Exception("Error while searching");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }
        userDataCache.setUserCurrentBotState(userId, BotState.IDLE);
    }
}
