package marshmallow.util;

import javax.annotation.Nonnull;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

public class RandomUtil {

    private static final List<String> characterSet = Arrays.asList(
            "q", "w", "e", "r", "t", "y", "u", "i", "o", "p", "a", "s", "d", "f", "g", "h", "j", "k", "l", "z", "x", "c", "v", "b", "n", "m",
            "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z", "X", "C", "V", "B", "N", "M",
            "0", "1", "2", "3", "4", "5", "6", "7", "9", "8", "!", "%", "&", "(", ")", "[", "]", "{", "}"
    );

    private static final SecureRandom random = new SecureRandom();

    public static boolean getBoolean() {
        return random.nextBoolean();
    }

    public static int getInteger(int bound) {
        if (bound <= 0) {
            return 0;
        }
        return random.nextInt(bound);
    }

    public static String pickRandom(@Nonnull String... strings) {
        return strings[random.nextInt(strings.length)];
    }

    public static Object pickRandom(@Nonnull List<?> strings) {
        return strings.get(random.nextInt(strings.size()));
    }

    public static String generateString(int length) {
        StringBuilder tokenBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            tokenBuilder.append(RandomUtil.pickRandom(characterSet));
        }
        return tokenBuilder.toString();
    }
}
