package hexlet.code.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class HtmlUtils {

    public static String getTimestampFormatted(Timestamp timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm").format(timestamp);
    }
}
