package marshmallow.util;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArrayUtil {

    private static final Pattern argumentsRegEX = Pattern.compile("([^\"]\\S*|\".+?\")\\s*", Pattern.MULTILINE);

    public static String[] toArguments(@Nonnull String string) {
        List<String> arguments = new ArrayList<>();

        Matcher matcher = argumentsRegEX.matcher(string);
        while (matcher.find()) {
            arguments.add(matcher.group(0)
                    .replaceAll("\"", "")
                    .trim());
        }

        return arguments.toArray(new String[0]);
    }
}
