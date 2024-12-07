package io.github.aapplet.wechat.common;

import io.github.aapplet.wechat.base.WeChatResponse;
import io.github.aapplet.wechat.exception.WeChatException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 下载处理
 */
@Getter
@RequiredArgsConstructor
public class WeChatDownload implements WeChatResponse.Download {

    private final byte[] bytes;

    /**
     * 保存文件
     *
     * @param path 保存路径
     */
    public void save(Path path) {
        try {
            Files.write(path, bytes);
        } catch (IOException e) {
            throw new WeChatException("保存失败,请检查路径是否正确", e);
        }
    }

}
