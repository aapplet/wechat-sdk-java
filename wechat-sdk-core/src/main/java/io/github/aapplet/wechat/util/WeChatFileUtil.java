package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 文件工具
 */
public class WeChatFileUtil {

    /**
     * 读取文件所有文本
     *
     * @param path 路径
     * @return 文本
     */
    public static String readString(String path) {
        return new String(readAllBytes(path), StandardCharsets.UTF_8);
    }

    /**
     * 读取文件所有字节
     *
     * @param path 路径
     * @return 字节
     */
    public static byte[] readAllBytes(String path) {
        try {
            return Files.readAllBytes(getPath(path));
        } catch (IOException e) {
            throw new WeChatException("文件加载失败,请检查路径是否正确", e);
        }
    }

    /**
     * 路径字符串转换为路径对象
     *
     * @param path 路径字符串
     * @return 路径对象
     */
    public static Path getPath(String path) {
        final URL url = ClassLoader.getSystemResource(path);
        if (url != null) {
            return new File(url.getPath()).toPath();
        } else {
            return Paths.get(path).toAbsolutePath();
        }
    }

}