package ru.fbtw.navigator.parent_navigation_bot.bot_api.concurent;

import lombok.SneakyThrows;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.MapperTelegramBot;

import java.util.concurrent.ConcurrentLinkedQueue;

public class AsyncMessageSender extends Thread{
    private final MapperTelegramBot mapperTelegramBot;
    private final ConcurrentLinkedQueue<ConcurrentItem> concurrentItems;

    public AsyncMessageSender(MapperTelegramBot mapperTelegramBot, ConcurrentLinkedQueue<ConcurrentItem> concurrentItems) {
        this.mapperTelegramBot = mapperTelegramBot;
        this.concurrentItems = concurrentItems;
    }

    @SneakyThrows
    @Override
    public void run() {
        super.run();
        while (true){
            while (!concurrentItems.isEmpty()){
                ConcurrentItem messages = concurrentItems.poll();
                for (SendPhoto photo : messages.getContent()) {
                    mapperTelegramBot.execute(photo);
                }
            }
        }
    }
}
