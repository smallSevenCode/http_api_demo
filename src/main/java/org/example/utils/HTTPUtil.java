package org.example.utils;

import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.example.pojo.Account;

import java.util.Map;

/**
 * HTTP请求工具类
 *
 * @author 苦瓜不苦
 * @date 2021/6/9 10:57
 **/
public class HTTPUtil {


    /**
     * post请求
     *
     * @param url 请求地址
     * @param map 参与加密的请求参数
     * @throws Exception
     */
    public static void post(String url, Map<String, Object> map) throws Exception {
        String jsonBody = JSON.toJSONString(map);
        // 账户配置
        Account account = new Account();
        // 签名信息
        String sign = RSAHelperUtil.generateSign(jsonBody);
        // 加密数据
        String encrypt = RSAHelperUtil.encrypt(jsonBody);
        // 加密后的请求参数
        map.clear();
        map.put("insId", account.getInsId());
        map.put("operId", account.getOperId());
        map.put("sign", sign);
        map.put("encrypt", encrypt);
        // 发送http请求
        String result = HttpRequest
                .post(url)
                .form(map)
                .execute()
                .body();
        // 解析返回参数
        JSONObject resultObject = JSON.parseObject(result);
        // 解密
        String resultEncrypt = resultObject.getString("encrypt");
        String resultData = RSAHelperUtil.decrypt(resultEncrypt);
        System.err.println("数据结果 >>>>>> " + resultData);
        // 验签
        String resultSign = resultObject.getString("sign");
        Boolean verifySign = RSAHelperUtil.verifySign(resultData, resultSign);
        System.err.println("验签结果 >>>>>> " + verifySign);
    }

}
