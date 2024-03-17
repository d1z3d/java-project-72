package hexlet.code.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtils {
    public static String getValueOfNodeByTag(String tag, String body) {
        String value = "";
        Pattern pattern = Pattern.compile(String.format("<%s>(.*)</%s>", tag, tag));
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            value = matcher.group(1);
        }
        return value;
    }

    public static String getTimestampFormatted(Timestamp timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
}
