package ru.fbtw.navigator.parent_navigation_bot.search;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessagePredictorService {
    private List<Pattern> fromToPatterns;
    private List<Pattern> toFromPatterns;
    private List<Pattern> confirmPatterns;
    private Pattern namesPattern;

    public MessagePredictorService(
            List<Pattern> fromToPatterns,
            List<Pattern> toFromPatterns,
            Set<String> nodesNames,
            List<Pattern> confirmPatterns
    ) {
        this.fromToPatterns = fromToPatterns;
        this.toFromPatterns = toFromPatterns;
        this.confirmPatterns = confirmPatterns;
        updatePattern(nodesNames);

    }

    public void updatePattern(Set<String> nodesNames) {
        String format = "(\\b%s\\b)|";
        StringBuilder builder = new StringBuilder();
        for(String name : nodesNames){
            builder.append(String.format(format,name.toLowerCase(Locale.ROOT)));
        }
        String pattern = builder.substring(0,builder.length()-1);
        namesPattern = Pattern.compile(pattern);
    }

    public List<String> splitMessage(String text){
        Matcher matcher;
        String preparedText = text.toLowerCase(Locale.ROOT);

        for(Pattern fromToPattern : fromToPatterns){
            matcher = fromToPattern.matcher(preparedText);
            if(matcher.find()){
                return findNames(preparedText,true);
            }
        }

        for(Pattern toFromPattern :toFromPatterns){
            matcher = toFromPattern.matcher(preparedText);
            if(matcher.find()){
                return findNames(preparedText,false);
            }
        }

        return new ArrayList<>();
    }

    private List<String> findNames(String text, boolean isFromTo) {
        ArrayList<String> res = new ArrayList<>();
        Matcher matcher = namesPattern.matcher(text);
        while (matcher.find()){
            res.add(matcher.group());
        }
        if(res.size() == 2){
            if(!isFromTo){
                Collections.reverse(res);
            }
        }

        return res;
    }

    public boolean isConfirmMessage(String userResponse) {
        for(Pattern confPattern : confirmPatterns){
            if(confPattern.matcher(userResponse).find()){
                return true;
            }
        }
        return false;
    }
}
