package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatPathException;
import io.github.aapplet.wechat.exception.WeChatReadException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * WeChatResourceUtil 类提供了文件读取的工具方法。
 * 主要功能是根据文件路径读取文件数据，支持外部文件和类路径文件的读取。
 * 如果外部文件读取失败，会尝试从类路径中读取文件。
 */
public class WeChatResourceUtil {

    /**
     * 类路径文件的伪 URL 前缀，用于标识文件路径为类路径资源。
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 读取文件数据。
     * <br>
     * 支持以下几种文件路径格式：
     * <ul>
     *   <li>外部文件：相对路径 "a/b.txt" 或绝对路径 "/a/b.txt"</li>
     *   <li>类路径文件：相对路径 "classpath:a/b.txt" 或绝对路径 "classpath:/a/b.txt"</li>
     * </ul>
     * 如果外部文件读取失败，会自动尝试从类路径中读取文件。
     *
     * @param filePath 文件路径
     * @return 文件的字节数据
     * @throws WeChatPathException 如果文件不存在或路径无效
     * @throws WeChatReadException 如果文件读取过程中发生 I/O 错误
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
     * 读取外部文件的字节数据。
     * <br>
     * 支持以下几种文件路径格式：
     * <ul>
     *   <li>相对路径："a/b.txt"</li>
     *   <li>绝对路径："/a/b.txt"</li>
     * </ul>
     * 如果文件不存在或路径无效，抛出 {@link WeChatPathException} 异常。
     * 如果读取过程中发生 I/O 错误，抛出 {@link WeChatReadException} 异常。
     *
     * @param filePath 文件路径
     * @return 文件的字节数据
     * @throws WeChatPathException 如果文件不存在或路径无效
     * @throws WeChatReadException 如果文件读取过程中发生 I/O 错误
     */
    public static byte[] readExternalFile(String filePath) {
        Path path = Path.of(filePath);
        // 检查文件是否存在且为普通文件
        if (!Files.isRegularFile(path)) {
            throw new WeChatPathException("Could not find resource: " + path);
        }
        try {
            // 读取文件的字节数据
            return Files.readAllBytes(path);
        } catch (IOException e) {
            // 如果读取失败，抛出读取异常
            throw new WeChatReadException("文件读取失败", e);
        }
    }

    /**
     * 读取类路径文件的字节数据。
     * <br>
     * 支持以下几种文件路径格式：
     * <ul>
     *   <li>相对路径："classpath:a/b.txt"</li>
     *   <li>绝对路径："classpath:/a/b.txt"</li>
     * </ul>
     * 如果文件不存在或路径无效，抛出 {@link WeChatPathException} 异常。
     * 如果读取过程中发生 I/O 错误，抛出 {@link WeChatReadException} 异常。
     *
     * @param filePath 文件路径，必须以 "classpath:" 开头
     * @return 文件的字节数据
     * @throws WeChatPathException 如果文件不存在或路径无效
     * @throws WeChatReadException 如果文件读取过程中发生 I/O 错误
     */
    public static byte[] readClassPathFile(String filePath) {
        // 去除路径中的 "classpath:" 前缀
        String path = filePath.replaceFirst(CLASSPATH_URL_PREFIX, "");
        // 尝试从类加载器中获取资源 URL
        URL url = WeChatResourceUtil.class.getClassLoader().getResource(path);
        if (url == null) {
            // 如果类加载器未能找到资源，尝试从当前类的资源中获取
            url = WeChatResourceUtil.class.getResource(path);
        }
        // 检查资源是否存在且为文件
        if (url == null || !new File(url.getFile()).isFile()) {
            throw new WeChatPathException("Could not find resource: " + filePath);
        }
        try (InputStream inputStream = url.openStream()) {
            // 读取文件的字节数据
            return inputStream.readAllBytes();
        } catch (IOException e) {
            // 如果读取失败，抛出读取异常
            throw new WeChatReadException("文件读取失败", e);
        }
    }

}