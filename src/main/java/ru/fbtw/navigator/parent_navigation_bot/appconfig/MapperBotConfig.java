package ru.fbtw.navigator.parent_navigation_bot.appconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.MapperTelegramBot;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.TelegramFacade;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class MapperBotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public MapperTelegramBot mapperTelegramBot(TelegramFacade facade){
        DefaultBotOptions options = ApiContext
                .getInstance(DefaultBotOptions.class);

        MapperTelegramBot telegramBot = new MapperTelegramBot(options,facade);

        telegramBot.setWebHookPath(webHookPath);
        telegramBot.setBotUserName(botUserName);
        telegramBot.setBotToken(botToken);

        return telegramBot;
    }

    @Bean
    public MessageSource messageSource(){
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
}
