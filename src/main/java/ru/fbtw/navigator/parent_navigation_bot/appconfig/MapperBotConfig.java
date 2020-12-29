package ru.fbtw.navigator.parent_navigation_bot.appconfig;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.MapperTelegramBot;
import ru.fbtw.navigator.parent_navigation_bot.bot_api.TelegramFacade;
import ru.fbtw.navigator.parent_navigation_bot.io.FileUtils;
import ru.fbtw.navigator.parent_navigation_bot.io.GraphJsonParser;
import ru.fbtw.navigator.parent_navigation_bot.navigation.Node;
import ru.fbtw.navigator.parent_navigation_bot.search.MessagePredictorService;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;


@Slf4j
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "telegrambot")
public class MapperBotConfig {
    private String webHookPath;
    private String botUserName;
    private String botToken;

    @Bean
    public MapperTelegramBot mapperTelegramBot(TelegramFacade facade) {
        DefaultBotOptions options = ApiContext
                .getInstance(DefaultBotOptions.class);

        MapperTelegramBot telegramBot = new MapperTelegramBot(options, facade);

        telegramBot.setWebHookPath(webHookPath);
        telegramBot.setBotUserName(botUserName);
        telegramBot.setBotToken(botToken);

        return telegramBot;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public SearchingService searchingService() {
        HashMap<String, Node> nodesStorage = new HashMap<>();
        HashSet<Node> privateNodes = new HashSet<>();
        try {
            File defaultEnv = new File("default_env.json");
            GraphJsonParser parser = new GraphJsonParser(defaultEnv);
            nodesStorage = parser.parse(privateNodes);

        } catch (FileNotFoundException e) {
            log.error("Failed to load the default configuration");
        } catch (IOException e) {
            log.error("The default configuration file is corrupted");
        }

        return new SearchingService(nodesStorage,privateNodes);
    }

    @Bean
    public MessagePredictorService messagePredictorService(SearchingService service) {
        Set<String> names = service.getNamesSet();

        List<Pattern> fromToPatterns = FileUtils.getPatternsFromFile("from_to.patterns");
        List<Pattern> toFromPatterns = FileUtils.getPatternsFromFile("to_from.patterns");
        List<Pattern> confirmPatterns = FileUtils.getPatternsFromFile("confirm.pattern");

        return new MessagePredictorService(fromToPatterns, toFromPatterns, names, confirmPatterns);

    }


}
