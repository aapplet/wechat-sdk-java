package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatPathException;
import io.github.aapplet.wechat.exception.WeChatReadException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class WeChatResourceUtil {

    /**
     * Pseudo URL prefix for loading from the class path: "classpath:".
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 读取文件数据
     * <br>
     * 外部文件: "a/b.txt" or "/a/b.txt"
     * <br>
     * 外部文件读取失败会读取类路径文件
     * <br>
     * 类路径文件: "classpath:a/b.txt" or "classpath:/a/b.txt"
     *
     * @param filePath 文件路径
     * @return 文件数据
     */
    public static byte[] readAllBytes(String filePath) {
        if (filePath.startsWith(CLASSPATH_URL_PREFIX)) {
            return readClassPathFile(filePath);
        } else {
            try {
                return readExternalFile(filePath);
            } catch (WeChatPathException e) {
                return readClassPathFile(filePath);
            }
        }
    }

    /**
     * 读取外部文件
     * <br>
     * 相对路径: "a/b.txt"
     * <br>
     * 绝对路径: "/a/b.txt"
     *
     * @param filePath 文件路径
     * @return 文件数据
     */
    public static byte[] readExternalFile(String filePath) {
        Path path = Path.of(filePath);
        if (!Files.isRegularFile(path)) {
            throw new WeChatPathException("Could not find resource: " + path);
        }
        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new WeChatReadException("文件读取失败", e);
        }
    }

    /**
     * 读取类路径文件
     * <br>
     * 相对路径: "classpath:a/b.txt"
     * <br>
     * 绝对路径: "classpath:/a/b.txt"
     *
     * @param filePath 文件路径
     * @return 文件数据
     */
    public static byte[] readClassPathFile(String filePath) {
        String path = filePath.replaceFirst(CLASSPATH_URL_PREFIX, "");
        URL url = WeChatResourceUtil.class.getClassLoader().getResource(path);
        if (url == null) {
            url = WeChatResourceUtil.class.getResource(path);
        }
        if (url == null || !new File(url.getFile()).isFile()) {
            throw new WeChatPathException("Could not find resource: " + filePath);
        }
        try (InputStream inputStream = url.openStream()) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new WeChatReadException("文件读取失败", e);
        }
    }

}