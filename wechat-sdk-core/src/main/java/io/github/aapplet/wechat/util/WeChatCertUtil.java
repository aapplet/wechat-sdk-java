package io.github.aapplet.wechat.util;

import io.github.aapplet.wechat.exception.WeChatException;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

/**
 * 微信证书工具类，用于处理商户证书的私钥和平台证书的公钥。
 */
@Slf4j
public class WeChatCertUtil {

    /**
     * 从指定文件路径读取私钥并解析为 {@link PrivateKey} 对象。
     *
     * @param filePath 私钥文件路径
     * @return 解析后的私钥对象
     * @throws WeChatException 如果当前 Java 环境不支持 RSA 或者私钥格式无效
     */
    public static PrivateKey getPrivateKey(String filePath) {
        byte[] bytes = WeChatResourceUtil.readAllBytes(filePath);
        String privateKey = new String(bytes, StandardCharsets.UTF_8)
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKey)));
        } catch (NoSuchAlgorithmException e) {
            throw new WeChatException("当前Java环境不支持RSA", e);
        } catch (InvalidKeySpecException e) {
            throw new WeChatException("无效的私钥", e);
        }
    }

    /**
     * 从字节数组中解析 X.509 证书并返回 {@link X509Certificate} 对象。
     *
     * @param bytes 证书的字节数组
     * @return 解析后的 X.509 证书对象
     * @throws WeChatException 如果证书格式无效
     */
    public static X509Certificate getCertificate(byte[] bytes) {
        try (InputStream inputStream = new ByteArrayInputStream(bytes)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            return (X509Certificate) cf.generateCertificate(inputStream);
        } catch (IOException | CertificateException e) {
            throw new WeChatException("无效的证书", e);
        }
    }

    /**
     * 将 X509 证书导出为 PEM 格式的文件。
     *
     * @param certificate 需要导出的 X509 证书对象
     * @param filePath    输出的文件路径，包含文件名。例如："path/to/certificate.pem"
     * @throws CertificateEncodingException 如果证书的编码过程中发生错误
     * @throws IOException                  如果文件操作过程中发生错误
     */
    public static void exportCertificateToPEM(X509Certificate certificate, String filePath) throws CertificateEncodingException, IOException {
        // 将证书对象转换为其编码后的字节数组（通常是 DER 格式）
        byte[] certificateBytes = certificate.getEncoded();
        // 将 DER 格式的字节数组编码为 Base64 字符串
        String certificateBase64 = Base64.getEncoder().encodeToString(certificateBytes);
        // 构建 PEM 格式的证书字符串
        // PEM 格式通常包含头部和尾部的标记（BEGIN CERTIFICATE 和 END CERTIFICATE），
        // 中间是 Base64 编码的证书数据，并且每个部分之间通常用换行符分隔
        String certificateString = "-----BEGIN CERTIFICATE-----\n" + certificateBase64 + "\n-----END CERTIFICATE-----\n";
        // 确保文件路径的父目录存在
        // Path.of(filePath) 用于将字符串路径转换为 Path 对象
        Path path = Path.of(filePath);
        // Files.notExists(path.getParent()) 检查文件的父目录是否不存在
        // 如果父目录不存在，Files.createDirectories(path.getParent()) 会递归创建所有必要的父目录
        if (Files.notExists(path.getParent())) {
            Files.createDirectories(path.getParent());
        }
        // 将构建好的 PEM 格式的证书字符串写入指定的文件路径
        // Files.writeString 是 JDK 11 引入的便捷方法，可以直接将字符串写入文件
        Files.writeString(path, certificateString);
        // 打印成功信息
        log.info("证书已成功导出为 PEM 格式到: {}", path.toAbsolutePath());
    }

}