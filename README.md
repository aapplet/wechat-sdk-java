# wechat-sdk-java

## 概览

- [x] 微信支付API v3
- [x] 微信公众平台
- [x] 微信小程序
- [x] 跨城容灾
- [x] 自动更新微信支付平台证书
- [x] 自动更新公众平台AccessToken

## 环境要求

- Java 11+

## 安装

### Maven

```xml

<dependency>
    <groupId>io.github.aapplet</groupId>
    <artifactId>wechat-sdk-all</artifactId>
    <version>0.0.4</version>
</dependency>
```

## 初始化

```java

@Configuration
public class WeChatConfiguration {

    @Bean
    WeChatClient client() {
        WeChatConfig wechatConfig = new WeChatConfig();
        wechatConfig.setAppId(".........Appid............");
        wechatConfig.setAppSecret(".....AppSecret........");
        wechatConfig.setMchId(".........MchId............");
        wechatConfig.setMchKey("........MchKey...........");
        wechatConfig.setServiceId(".....ServiceId........");
        wechatConfig.setSerialNo("......SerialNo.........");
        wechatConfig.loadPrivateKey("...PrivateKey.pem...");
        return new DefaultWeChatClient(wechatConfig);
    }

}
```

### 示例：JSAPI下单

```java
        WeChatClient client = client();
WeChatPaymentJsapiRequest request = new WeChatPaymentJsapiRequest();
        request.

setDescription("JSAPI下单");
        request.

setOutTradeNo("OutTradeNo");
        request.

setNotifyUrl("https://xx.xx.xx/notify");
        request.

setAmount(new Amount().

setTotal(1));
        request.

setPayer(new Payer().

setOpenId("o4mxp5PnCt6TLrPVmUtG0Xsj8N-I"));
WeChatPaymentJsapiResponse response = client.execute(request);
WeChatPaymentJsapiResponse.JsapiResult jsapi = response.jsapi(client.getWeChatConfig());
        System.out.

println(jsapi);
```

## 回调通知的验签与解密

```java

@RestController
@RequiredArgsConstructor
public class NotifyController {

    private final WeChatClient wechatClient;

    @PostMapping("/notify")
    public void notify(@RequestHeader HttpHeaders headers, @RequestBody String body) {
        final Map<String, String> map = headers.toSingleValueMap();
        final WeChatConfig wechatConfig = wechatClient.getWeChatConfig();
        final WeChatNotifyHandler handler = new WeChatNotifyHandler(wechatConfig, map, body);
        final WeChatPaymentNotify paymentNotify = handler.transform(WeChatPaymentNotify.class);
        System.out.println(paymentNotify);
    }

}
```

## 联系我们

如果你发现了**BUG**或者有任何疑问、建议，请通过issue进行反馈。