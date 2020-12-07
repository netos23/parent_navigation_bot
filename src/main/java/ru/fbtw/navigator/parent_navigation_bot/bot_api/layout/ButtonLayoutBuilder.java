package ru.fbtw.navigator.parent_navigation_bot.bot_api.layout;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

public class ButtonLayoutBuilder {
    private List<List<InlineKeyboardButton>> layout;
    private List<InlineKeyboardButton> editRow;
    private boolean isRowSubmitted;

    public ButtonLayoutBuilder() {
        layout = new ArrayList<>();
    }

    public ButtonLayoutBuilder addRow(){
        editRow = new ArrayList<>();
        isRowSubmitted = false;
        return this;
    }

    public ButtonLayoutBuilder addToLastRow(InlineKeyboardButton button){
        if(!isRowSubmitted) {
            editRow.add(button);
        }
        return this;
    }

    public ButtonLayoutBuilder submitRow(){
        if(!isRowSubmitted) {
            layout.add(editRow);
            isRowSubmitted = true;
        }
        return this;
    }

    public List<List<InlineKeyboardButton>> build(){
        submitRow();
        return layout;
    }
}
