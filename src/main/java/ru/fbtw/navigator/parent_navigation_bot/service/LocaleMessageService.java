package ru.fbtw.navigator.parent_navigation_bot.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import java.util.Locale;

@Service
public class LocaleMessageService {
    private final Locale locale;
    private final MessageSource messageSource;

    public LocaleMessageService(@Value("${localeTag}") String locale, MessageSource messageSource) {
        this.locale = Locale.forLanguageTag(locale);
        this.messageSource = messageSource;
    }

    public String getMessage(String message) {
        return messageSource.getMessage(message,null,locale);
    }
}
