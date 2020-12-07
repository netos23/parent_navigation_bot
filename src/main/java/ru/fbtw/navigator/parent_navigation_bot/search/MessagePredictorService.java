package ru.fbtw.navigator.parent_navigation_bot.search;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class MessagePredictorService {
    private List<Pattern> fromToPatterns;
    private List<Pattern> toFromPatterns;
    private Pattern namesPattern;

    public MessagePredictorService(
            List<Pattern> fromToPatterns,
            List<Pattern> toFromPatterns,
            Set<String> nodesNames) {
        this.fromToPatterns = fromToPatterns;
        this.toFromPatterns = toFromPatterns;
        updatePattern(nodesNames);

    }

    public void updatePattern(Set<String> nodesNames) {
        String format = "(\\b%s\\b)|";
        StringBuilder builder = new StringBuilder();
        for(String name : nodesNames){
            builder.append(String.format(format,name));
        }
        String pattern = builder.substring(0,builder.length()-1);
        namesPattern = Pattern.compile(pattern);
    }

    public List<String> splitMessage(String text){
        Matcher matcher;

        for(Pattern fromToPattern : fromToPatterns){
            matcher = fromToPattern.matcher(text);
            if(matcher.find()){
                return findNames(text,true);
            }
        }

        for(Pattern toFromPattern :toFromPatterns){
            matcher = toFromPattern.matcher(text);
            if(matcher.find()){
                return findNames(text,false);
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

}
