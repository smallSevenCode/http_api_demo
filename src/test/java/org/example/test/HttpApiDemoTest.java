package org.example.test;

import cn.hutool.core.util.IdUtil;
import org.example.utils.HTTPUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 苦瓜不苦
 * @date 2022/8/10 12:00
 */
public class HttpApiDemoTest {

    private final static String url = "http://10.18.6.25:8085/xlzxop/v2/creditreport/getEnterprisePersonInfoV5.do";

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
        paramMap.put("idcard", "110108196902151473");
        paramMap.put("custSerialNo", IdUtil.simpleUUID());
        HTTPUtil.post(url, paramMap);
    }

}
