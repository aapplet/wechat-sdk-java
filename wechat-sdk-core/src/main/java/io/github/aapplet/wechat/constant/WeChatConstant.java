package io.github.aapplet.wechat.constant;

/**
 * 常量
 */
public final class WeChatConstant {

    /**
     * 应答类型
     */
    public static final String ACCEPT = "Accept";
    /**
     * 用户代理
     */
    public static final String USER_AGENT = "User-Agent";
    /**
     * 数据类型
     */
    public static final String CONTENT_TYPE = "Content-Type";
    /**
     * http认证
     */
    public static final String AUTHORIZATION = "Authorization";
    /**
     * json格式
     */
    public static final String APPLICATION_JSON = "application/json";

    /**
     * 示例：WeChat-SDK-Java (Windows 11/10.0) Java/11.0.0
     */
    public static final String USER_AGENT_VALUE = String.format(
            "WeChat-Java (%s/%s) Java/%s",
            System.getProperty("os.name"),
            System.getProperty("os.version"),
            System.getProperty("java.version")
    );

}
