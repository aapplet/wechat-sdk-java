package io.github.aapplet.wechat.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 提供了生成随机字符串的工具方法。
 */
public class WeChatRandomUtil {

    /**
     * 定义了生成随机字符串时所使用的字符集。
     * 包含数字（0-9）、小写字母（a-z）和大写字母（A-Z）。
     */
    private static final char[] SYMBOLS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 生成指定长度的随机字符串。
     *
     * @param length 指定生成的随机字符串的长度。
     * @return 生成的随机字符串。
     */
    public static String random(int length) {
        var random = ThreadLocalRandom.current();
        var chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = SYMBOLS[random.nextInt(SYMBOLS.length)];
        }
        return new String(chars);
    }

    /**
     * 生成固定长度为32的随机字符串。
     *
     * @return 长度为32的随机字符串。
     */
    public static String random32() {
        return random(32);
    }

}