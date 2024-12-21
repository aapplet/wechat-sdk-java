package io.github.aapplet.util;

import io.github.aapplet.wechat.util.RetryTemplate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Slf4j
public class RetryTests {

    @Test
    void simulate() {
        Supplier<String> supplier = () -> {
            int nextInt = ThreadLocalRandom.current().nextInt(100);
            if (nextInt < 50) {
                throw new RuntimeException("模拟操作失败");
            }
            return "操作成功";
        };
        try {
            assert supplier.get() == null;
            String result = RetryTemplate.submit(supplier, 10, RuntimeException.class);
            System.out.println("最终结果：" + result);
        } catch (Exception e) {
            System.out.println("最终失败，原因：" + e.getMessage());
        }
    }

}
