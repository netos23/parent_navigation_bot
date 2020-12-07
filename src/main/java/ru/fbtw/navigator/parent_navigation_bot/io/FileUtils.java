package ru.fbtw.navigator.parent_navigation_bot.io;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

@Slf4j
public class FileUtils {
    public static List<Pattern> getPatternsFromFile(String filename) {
        File file = new File(filename);
        return getPatternsFromFile(file);
    }

    public static List<Pattern> getPatternsFromFile(File file) {
        log.info("reading patterns from {}", file.getPath());

        List<Pattern> patterns = new ArrayList<>();
        try {
            Scanner in = new Scanner(file);
            while (in.hasNext()){
                String stringPattern = in.nextLine();
                patterns.add(Pattern.compile(stringPattern));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            log.error("error while reading from file{}",file.getPath());
        }

        return patterns;
    }
}
