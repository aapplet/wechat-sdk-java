package io.github.aapplet.wechat.util;

import java.util.concurrent.ThreadLocalRandom;

/**
 * 随机字符串工具类
 */
public class WeChatRandomUtil {

    private static final char[] SYMBOLS = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * 生成随机字符串
     *
     * @param length 字符串长度
     * @return 随机字符串
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
     * @return 32位随机字符串
     */
    public static String random32() {
        return random(32);
    }

}