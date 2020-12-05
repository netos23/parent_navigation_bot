package ru.fbtw.navigator.parent_navigation_bot.controller;

import jdk.nashorn.internal.ir.RuntimeNode;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.MapperTelegramBot;

@RestController
public class WebHookController {
    private final MapperTelegramBot telegramBot;

    public WebHookController(MapperTelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @RequestMapping(value = "/",method = RequestMethod.POST)
    public BotApiMethod<?> onUpdateReceived(@RequestBody Update update){
        return telegramBot.onWebhookUpdateReceived(update);
    }
}
