package ru.fbtw.navigator.parent_navigation_bot.bot_api.hendelrs.search;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SearchItem {
    String from;
    String to;
}
