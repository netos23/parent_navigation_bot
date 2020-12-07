package ru.fbtw.navigator.parent_navigation_bot.search;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.fbtw.navigator.parent_navigation_bot.io.FileUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

class MessagePredictorServiceTest {
    private MessagePredictorService predictorService;

    @BeforeEach
    void setUp() {
        Set<String> fileNames = new HashSet<String>(){{
            add("кв. 77");
            add("лицей №3");
            add("Вгу университет");
            add("Серф");
        }};

        List<Pattern> fromToPatterns = FileUtils.getPatternsFromFile("from_to.patterns");
        List<Pattern> toFromPatterns = FileUtils.getPatternsFromFile("to_from.patterns");
        List<Pattern> confirmPatterns = FileUtils.getPatternsFromFile("confirm.patterns");

        predictorService = new MessagePredictorService(fromToPatterns,toFromPatterns,fileNames, confirmPatterns);
    }



    @Test
    void splitMessage() {
        String testMessage = "из кв. 77 в лицей №3";

        List<String> namesList = predictorService.splitMessage(testMessage);

        Assertions.assertEquals(2,namesList.size());
    }
}