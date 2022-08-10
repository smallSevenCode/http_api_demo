package org.example.test;

import org.example.utils.HTTPUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zp
 * @date 2022/8/10 12:00
 */
public class HttpApiDemoTest {

    private final static String url = "http://127.0.0.1/xxx/xxx/xxx.do";

    /**
     * 发送请求
     * 替换工具类中的公钥、私钥、机构标识、操作员
     *
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("xxx", "xxx");
        map.put("yyy", "yyy");
        map.put("zzz", "zzz");
        HTTPUtil.post(url, map);
    }

}
