package org.example.test;

import org.example.utils.HTTPUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 苦瓜不苦
 * @date 2022/8/10 12:00
 */
public class HttpApiDemoTest {

    private final static String url = "http://127.0.0.1:8080/xlzxop/v2/***.do";

    /**
     * 发送请求
     * 替换Account类中的公钥、私钥、机构标识、操作员
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("xxx", "xxx");
        paramMap.put("yyy", "yyy");
        paramMap.put("zzz", "zzz");
        HTTPUtil.post(url, paramMap);
    }

}
