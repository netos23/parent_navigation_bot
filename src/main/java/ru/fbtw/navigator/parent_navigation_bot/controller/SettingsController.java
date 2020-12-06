package ru.fbtw.navigator.parent_navigation_bot.controller;

import org.springframework.web.bind.annotation.*;
import ru.fbtw.navigator.parent_navigation_bot.search.SearchingService;

@RestController
public class SettingsController {

    private SearchingService searchingService;

    public SettingsController(SearchingService searchingService) {
        this.searchingService = searchingService;
    }

    @RequestMapping(value = "/api/update", method = RequestMethod.POST)
    public String update(
            @RequestParam String apiKey,
            @RequestBody String updateData
    ) {
        return "";
    }
}
