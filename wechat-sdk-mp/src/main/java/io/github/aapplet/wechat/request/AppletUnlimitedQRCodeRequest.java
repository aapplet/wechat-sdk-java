package io.github.aapplet.wechat.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.aapplet.wechat.WeChatMediaPlatformAttribute;
import io.github.aapplet.wechat.base.WeChatRequest;
import io.github.aapplet.wechat.config.WeChatConfig;
import io.github.aapplet.wechat.response.AppletUnlimitedQRCodeResponse;
import io.github.aapplet.wechat.util.WeChatUtil;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 获取小程序全局唯一后台接口调用凭据（access_token）。调用绝大多数后台接口时都需使用 access_token，开发者需要进行妥善保存。
 * <p>
 * https://developers.weixin.qq.com/miniprogram/dev/OpenApiDoc/qrcode-link/qr-code/getUnlimitedQRCode.html
 */
@Data
@Accessors(chain = true)
@SuppressWarnings("SpellCheckingInspection")
public class AppletUnlimitedQRCodeRequest implements WeChatRequest<AppletUnlimitedQRCodeResponse> {

    /**
     * 接口调用凭证，该参数为 URL 参数，非 Body 参数。
     */
    private String accessToken;

    /**
     * 必填：是
     * <p>
     * scene 字段的值会作为 query 参数传递给小程序/小游戏。用户扫描该码进入小程序/小游戏后，开发者可以获取到二维码中的 scene 值，再做处理逻辑。
     */
    @JsonProperty("scene")
    private String scene;

    /**
     * 必填：是
     * <p>
     * 默认是主页，页面 page，例如 pages/index/index，根路径前不要填加 /，不能携带参数（参数请放在 scene 字段里），如果不填写这个字段，默认跳主页面。
     */
    @JsonProperty("page")
    private String page;

    /**
     * 默认是true，检查page 是否存在，为 true 时 page 必须是已经发布的小程序存在的页面（否则报错）；为 false 时允许小程序未发布或者 page 不存在， 但page 有数量上限（60000个）请勿滥用。
     */
    @JsonProperty("check_path")
    private Boolean checkPath;

    /**
     * 要打开的小程序版本。正式版为 "release"，体验版为 "trial"，开发版为 "develop"。默认是正式版。
     */
    @JsonProperty("env_version")
    private String envVersion;

    /**
     * 默认430，二维码的宽度，单位 px，最小 280px，最大 1280px
     */
    @JsonProperty("width")
    private Integer width;

    /**
     * 自动配置线条颜色，如果颜色依然是黑色，则说明不建议配置主色调，默认 false
     */
    @JsonProperty("auto_color")
    private Boolean autoColor;

    /**
     * 默认是{"r":0,"g":0,"b":0} 。auto_color 为 false 时生效，使用 rgb 设置颜色 例如 {"r":"xxx","g":"xxx","b":"xxx"} 十进制表示
     */
    @JsonProperty("line_color")
    private lineColor lineColor;

    /**
     * 默认是false，是否需要透明底色，为 true 时，生成透明底色的小程序码
     */
    @JsonProperty("is_hyaline")
    private Boolean isHyaline;

    @Data
    public static class lineColor {
        /**
         * rgb-r值
         */
        @JsonProperty("r")
        private Integer r;
        /**
         * rgb-g值
         */
        @JsonProperty("g")
        private Integer g;
        /**
         * rgb-b值
         */
        @JsonProperty("b")
        private Integer b;
    }

    @Override
    public WeChatMediaPlatformAttribute<AppletUnlimitedQRCodeResponse> getAttribute(WeChatConfig weChatConfig) {
        WeChatMediaPlatformAttribute<AppletUnlimitedQRCodeResponse> attribute = new WeChatMediaPlatformAttribute<>();
        attribute.setMethod("POST");
        attribute.setDomain("https://api.weixin.qq.com/wxa/getwxacodeunlimit");
        attribute.setParameters("access_token=" + accessToken);
        attribute.setRequestBody(WeChatUtil.toJson(this));
        attribute.setResponseClass(AppletUnlimitedQRCodeResponse.class);
        return attribute.init();
    }


}