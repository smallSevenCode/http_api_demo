package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动类
 *
 * @author 苦瓜不苦
 * @date 2022/8/10 11:39
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class HttpApiDemoApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(HttpApiDemoApplication.class, args);
    }

}
