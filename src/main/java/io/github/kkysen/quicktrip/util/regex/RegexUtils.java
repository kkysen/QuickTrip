package io.github.kkysen.quicktrip.util.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * 
 * @author Khyber Sen
 */
public class RegexUtils {
    
    private static final String urlPattern = //
            "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    
    public static List<String> findMatches(final CharSequence s, final String pattern) {
        final Matcher matcher = Pattern.compile(pattern).matcher(s);
        final List<String> matches = new ArrayList<>();
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }
    
    public static List<String> findUrls(final CharSequence s) {
        return findMatches(s, urlPattern);
    }
    
}
